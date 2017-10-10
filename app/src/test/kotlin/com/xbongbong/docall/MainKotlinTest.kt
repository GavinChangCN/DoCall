package com.xbongbong.docall

@RunWith(AndroidJUnit4::class)
class MainKotlinTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        assertEquals("cc.duduhuo.kotlintest", appContext.packageName)
    }
}