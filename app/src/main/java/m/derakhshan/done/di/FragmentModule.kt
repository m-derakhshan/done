package m.derakhshan.done.di


import android.content.Context
import com.aminography.primecalendar.persian.PersianCalendar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import m.derakhshan.done.Arrange
import m.derakhshan.done.Utils
import m.derakhshan.done.main.today_tasks.TodayTaskRecyclerAdapter


@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    fun provideTodayTaskRecyclerAdapter() = TodayTaskRecyclerAdapter(PersianCalendar())

    @Provides
    fun provideUtils(@ApplicationContext context: Context) = Utils(context)

    @Provides
    fun provideArrange() = Arrange()
}