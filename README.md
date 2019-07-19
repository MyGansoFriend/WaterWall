# WaterWall
Multifunctional RecyclerVIew library with sections/states support

[![Build Status](https://travis-ci.com/LuckyLittleSparrow/WaterWall.svg?branch=master)](https://travis-ci.com/LuckyLittleSparrow/WaterWall)
[![codecov](https://codecov.io/gh/LuckyLittleSparrow/WaterWall/branch/master/graph/badge.svg)](https://codecov.io/gh/LuckyLittleSparrow/WaterWall)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=102)](https://opensource.org/licenses/Apache-2.0)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/LuckyLittleSparrow/WaterWall/blob/master/LICENSE)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
 [ ![Download](https://api.bintray.com/packages/luckylittlesparrow/WaterWall/com.luckylittlesparrow.waterwall/images/download.svg) ](https://bintray.com/luckylittlesparrow/WaterWall/com.luckylittlesparrow.waterwall/_latestVersion)


WaterWall will help you with implementing list structures in your application. Helpfulness is also about saving time and making your day a little bit easier, the main purpose of this library is to save time of developer on implementing again and againg the same tasks with UI lists. 
Most of the cool features you could search for before on the Internet to implement in your app are now available out of the box without putting any effort into implementing them.


## Add Dependency
Use Gradle:
```groovy
implementation 'com.luckylittlesparrow:waterwall:1.0.4'
```
or Maven:
```xml
<dependency>
  <groupId>com.luckylittlesparrow</groupId>
  <artifactId>waterwall</artifactId>
  <version>1.0.4</version>
  <type>pom</type>
</dependency>
```

## Quickstart


### Declare a Section class

All you need it is to implement section class with settings you need to use. Minimal section example :

### Simple section 

```kotlin
class SimpleSection(
    private val itemClickListener: (ExampleItem) -> Unit,
    contentItems: List<ExampleItem>
) : Section<Void, ExampleItem, Void>(contentItems = contentItems) {

    override fun getItemViewHolder(view: View): BaseViewHolder<ExampleItem> {
        return ItemViewHolder(view, itemClickListener)
    }

    override fun getSectionParams(): SectionParams {
        return SectionParams.builder()
            .itemResourceId(R.layout.section_item)
            .build()
    }

    override fun getDiffUtilItemCallback(): DiffUtilItemCallback {
        return object : DiffUtilItemCallback() {
            override fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean {
                return (oldItem as Item).name == (newItem as Item).name
            }

            override fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer) =
                (oldItem as Item).content == (newItem as Item).content
        }
    }
}
```

### Filterable section 

```kotlin
class FilterSection(
    headerItem: FilterHeader,
    contentItems: List<Item>,
    footerItem: FilterFooter
) : FilterableSection<FilterHeader, Item, FilterFooter>(headerItem, contentItems, footerItem) {

    override fun itemFilter(search: String, item: ItemContainer): Boolean {
        return (item as Item).body.contains(search, true)
    }
...
```

## Configure section params

You can see all available options in SectionParams class, some of them support only specific type of Section :

### Simple section supported functionality:  
- States
- Decorations
- Expandable sections
- Sticky headers
- Show more, show less

### Filterable section supported functionality:  
- States
- Decorations
- Sticky headers
- Filter by items
- Filter by headers


## Usage
After section declaration, just add it to the Adapter and control states through calls to the section class
```kotlin

   private val clickListener = { item: Item ->
        Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show()
    }

   private val sectionAdapter = SimpleSectionedAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val section = SimpleSection(clickListener, getExamples())
        sectionAdapter.setDefaultOptimizationSettings()
        sectionAdapter.addSection(section)
    }
```

Filter usage :

```kotlin
 searchViewEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                sectionAdapter.filter(editable.toString())
            }
        })
```

If you have multiple sections and if you don't want to create section before loading data, you can add StubSection class to adapter to show states of Recycler View until your data will be ready. After you add your own sections to adapter, stub section will be automaticly removed from the Adapter.

```kotlin
   private val stubSection = StubSection(
        R.layout.section_empty,
        R.layout.section_failed,
        R.layout.section_loading
    )
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sectionAdapter.setDefaultOptimizationSettings()
        stubSection.state = SectionState.EMPTY
        sectionAdapter.addSection(stubSection)
        stubSection.state = SectionState.LOADING
```

## States control
To change view state presentation use setter to section.state and adapter will update section automaticly.
```kotlin

        stateFailedButton.setOnClickListener { section.state = SectionState.FAILED }
        
        stateLoadingButton.setOnClickListener { section.state = SectionState.LOADING }
        
        stateLoadedButton.setOnClickListener {
            section.state = SectionState.LOADED
            section.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNumbersList()))
        }
        
        stateEmptyButton.setOnClickListener { section.state = SectionState.EMPTY }
```

## Examples

- [States usage example](app/src/main/java/com/luckylittlesparrow/waterwall/example/state/StateListFragment.kt)
- [Filter section example](app/src/main/java/com/luckylittlesparrow/waterwall/example/sectionedlist/FilterListFragment.kt)
- [Expandable section example](app/src/main/java/com/luckylittlesparrow/waterwall/example/expand/ExpandableListFragment.kt)
- [Stub section example](app/src/main/java/com/luckylittlesparrow/waterwall/example/stub/StubListFragment.kt)

## Reporting issues

Found a bug or a problem on a specific feature? Open an issue on [Github issues](https://github.com/LuckyLittleSparrow/WaterWall/issues)

## Getting Help

Any question about WaterWall usage? 
- Don't be afraid to write me in telegram, I will be glad to help, if I can [telegram](https://t.me/LuckySparrow)
- Post your question on issues section [Github issues](https://github.com/LuckyLittleSparrow/WaterWall/issues)

## Future releases
Will be added support for:
- animations (only default supported right now)
- snapping
- swipe to dismiss
- search
- drag and drop items
- horizontal scroll aka rv in rv
- scroll to section

## Copyright

    Copyright 2019 Gusev Andrei

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
