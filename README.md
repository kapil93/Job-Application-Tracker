# Job Application Tracker

### Overview
Searching for the right job could be quite laborious and time-consuming. During this quest, the number of jobs one might apply for could be overwhelming to keep track of.

Job Application Tracker helps you to organize and keep track of interviews, events, offers, company location, contact information and more!

Additionally, you don't need to be concerned about privacy at all as your data never leaves your device!

Primarily, I made this app to explore MVVM + Clean Architecture and also to keep track of my job applications.

<br>

### Need for Clean Architecture
I would firstly like to admit that for this super simple app implementing Clean Architecture is without a doubt over-engineering. But I would like to mention a few points about its benefits in big and complex apps.

For simple apps, it makes complete sense for the view model to interact directly with the repository, but we need some change in the architecture to accommodate the scenario when a single-use case depends on multiple repositories or feature modules. While we pay so much attention to the presentation layer, the repository code could become more and more complicated and messy. If we follow the clean architecture, the view model in the presentation layer would request for a particular data from the domain layer and it's this domain layer's responsibility to check authorization, resolve dependencies and retrieve the data from one or more repositories. It would also expose the repository interfaces to the data layer. This would make the repository implementations pretty straightforward. Since the domain layer is in the middle here, it gets to decide the app-specific business rules. The domain layer tells the presentation layer about the feature set the app supports, i.e., the data it could possibly fetch for it and the domain layer tells the data layer to implement repositories conforming to given specifications (repository interfaces). It also tells other layers about the structure of data it accepts. Once again, if we are interested in making an iOS app now, we could export the domain module (a pure Kotlin module) as it is, which contains most of the business rules, and we would only have to implement the data and presentation layers. There is some scope to share code in these layers as well, but let's save it for another day.

Read more about the high-level concepts surrounding clean architecture <a target="_blank" rel="noopener noreferrer" href="https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html">here</a>.

<br>

### View Model with Live Data
<br>

#### Lifecycle Awareness and Retention of View State
The simple fact that the view model component paired with live data elements are lifecycle aware changes the game dramatically. If we compare it to the presenter in MVP pattern, the view model saves us a considerable amount of code which would be otherwise written to bind and unbind the presenter with the view and to handle the fetching of view state from the presenter. Since the view model can also handle saving of view state during background process kills, it could be said that the view model provides a wholesome solution to retain view state depending on the lifecycle.

<br>

#### Responsibilities of components
The fact that the view model doesn't hold a reference to the view makes the code cleaner and clearly separates the responsibilities of each component. But, the same phenomenon causes the view to handle more responsibilities and in turn contain more logic as compared to the MVP pattern. To make it more clear, in MVP pattern it was the responsibility of the presenter to dictate the change in the view based on some logic which could be tested by testing the presenter, but in MVVM it is the responsibility of the view to react to the changes in the data held by the view model and hence we need to test the view as well. As a side note, it is in our best interest to keep the activity or fragment as dumb and logic free as possible because they are not easily testable compared to Kotlin or Java classes. This possible shortcoming could be avoided by moving almost all the logic concerning the view state to the view model and exposing the observable for this view state. Then again, the view model is also an Android component and it would be better for us if we could further move it to another class. So, we finally create a view entity class which holds all the details regarding the view state. This class now could easily be testable. Now, the responsibilities of the view model are reduced to fetching data from the repositories when requested, map it to view entity which the view could parse and remember the view state when changed.

<br>

#### Testability
Even after great efforts of moving most of the logic to simple Kotlin or java classes, there could be many such scenarios when the testing of the activities, fragments or view models becomes imperative. Even though I didn't dive too deep into writing tests around view models, I found that mocking the view model to test the activity could be somewhat tricky. In my case, I was not able to intercept the call to the private method "clear" belonging to the view model. It is called when the activity is finished, i.e., when we attempt to run multiple tests at a time. At least for my case, the obvious answer was to use test-orchestrator. I'm somewhat sure that there must be ways to write tests for and around view models, but the highlight here is that MVP would be a clear winner if we make a comparison.

<br>

#### Concerns about going Multiplatform
In my current company, we design and engineer mobile applications and we noticed that in almost all the cases the Android and iOS apps hugely overlap in terms of feature set and composition of screens, ignoring tiny platform-specific differences. Therefore, going multiplatform is of immense interest to us and could give us a competitive edge. If we view the comparison through the lens of multiplatform compatibility, it could be seen that MVP has a slight edge here as there will be less platform-specific code involved and thus more code could be shared amongst various platforms.

<br>

### Koin
Although there're no two ways about the fact that Dagger is currently unbeatable in terms of features and performance, the lightweight API and Kotlin friendliness that Koin offers is hard to ignore. The only major shortcoming of Koin is runtime dependency resolution. In small to medium-sized apps, we could get away with this shortcoming but as the app size increases and in turn the dependency graph becomes bigger, the app initialization time will increase and we will run a slightly greater risk of a crash even in the release version if we miss passing a rarely accessed dependency to Koin.

<br>

### What I didn't do
* Didn't create an elegant API for navigation as there were only 3 screens in the apps
* Didn't handle the restoration of UI state after background process kills through Saved State Handle as it was not needed in this app
* Didn't implement data binding because I neither see it improving the design nor reducing the volume of code
* Didn't write extensive tests

<br>

### Libraries Used
+ Koin
+ RxJava
+ Realm DB
+ Timber
+ JUnit
+ Espresso
+ MockK
