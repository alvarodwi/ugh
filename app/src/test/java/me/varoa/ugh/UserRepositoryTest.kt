package me.varoa.ugh

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import me.varoa.ugh.core.data.UserRepositoryImpl
import me.varoa.ugh.core.data.remote.api.GithubApiService
import me.varoa.ugh.core.data.remote.json.UserJson
import me.varoa.ugh.core.data.toModel
import me.varoa.ugh.core.domain.repository.UserRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

internal class UserRepositoryTest {
    @MockK
    lateinit var api: GithubApiService
    private lateinit var repository: UserRepository
    private val dispatcher = Dispatchers.Unconfined

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(api)
    }

    @Test
    fun `fetch user detail from api`() = runTest {
        val expected = UserJson(id = 1, username = "test", avatar = "")

        coEvery {
            api.getUserDetail("test")
        } returns Response.success(expected)
        runBlocking { api.getUserDetail("test") }

        // verify if above function invoked at least 1 time
        coVerify(atLeast = 1) { api.getUserDetail("test") }
        confirmVerified(api)

        // assertEquals of expected and mock api when called from repository function
        // repository function returns `User` so expected should be mapped into `User` first
        runBlocking {
            repository.getUser("test").test {
                assertEquals(
                    expected.toModel(),
                    expectMostRecentItem().getOrNull()
                )
                expectNoEvents()
            }
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
