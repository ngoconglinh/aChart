[![](https://jitpack.io/v/ngoconglinh/aChart.svg)](https://jitpack.io/#ngoconglinh/aChart)

## Quick Start

**ComplexView** is available on jitpack.

Add dependency:

```groovy
implementation "com.github.ngoconglinh:aChart:last-release"
```

## Usage

to use **aChart**:

in **Setting.gradle**
```groovy
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
```xml

<com.ngoconglinh.achart.Chart
    android:id="@+id/chart"
    android:layout_width="match_parent"
    android:layout_height="300dp"
    android:alpha="0.6"
    app:maxDay="31"
    app:column="7"
    app:isFormatNumber="true"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"/>
```


**Demo**
![alt tag](https://raw.ngoconglinh.com/aChart/master/screenshots/image.png)
<br/><br/>


**<h2="social">Use</h2>**
<br/><br/>
```kotlin
    aChart.addChartPoint(MutableList)
    aChart.setColorEnable(Boolean)
    aChart.mMaxDay = Int
```


<h2 id="creators">Special Thanks :heart:</h2>
