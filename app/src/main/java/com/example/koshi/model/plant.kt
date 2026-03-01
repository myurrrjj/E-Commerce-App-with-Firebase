package com.example.koshi.model

import androidx.annotation.Keep
import java.text.NumberFormat
import java.util.Locale

@Keep
data class ImageUrls(
    val original: String = "",
    val thumbnail: String = ""
)

@Keep
data class Plant(
    val id: Int = 0,
    val name: String = "",
    val price: Int = 0,
    val category: String = "",
    val images: List<ImageUrls> = emptyList(),
    val light: String = "",
    val height: String = "",
    val description: String = "",
    val careInstructions: String = ""
) {
    val displayPrice: String
        get() {
            val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            return format.format(price / 1.00)
        }
}

data class PlantCategory(val id: Int, val name: String)

val plantCategories = listOf(
    PlantCategory(1, "Indoor"),
    PlantCategory(2, "Outdoor"),
    PlantCategory(3, "Aromatic"),
    PlantCategory(4, "Succulent"),
    PlantCategory(5, "Hanging"),
    PlantCategory(6, "Herb"),
    PlantCategory(7, "Fruit Tree"),
    PlantCategory(8, "Vining"),
    PlantCategory(9, "Tree"),
    PlantCategory(10, "Medicinal"),
    PlantCategory(11, "Spice"),
    PlantCategory(12, "Spice/Mineral"),
    PlantCategory(13, "Flowering")
)


//val featuredPlantss = listOf(
//
//
//    Plant(
//        id = 2,
//        name = "Snake Plant",
//        price = "$30.00",
//        category = "Indoor",
//        images = listOf(
//            R.drawable.snake,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Indirect",
//        height = "30-40cm",
//        description = "Sansevieria trifasciata, commonly known as the snake plant or mother-in-law's tongue, is a species of flowering plant in the family Asparagaceae, native to tropical West Africa. It is an evergreen perennial plant forming dense stands, spreading by way of its creeping rhizome.",
//        careInstructions = "Tolerant of low light and irregular watering. It's a great air purifier and very low maintenance."
//    ),
//    Plant(
//        id = 3,
//        name = "Monstera Deliciosa",
//        price = "$55.00",
//        category = "Indoor",
//        images = listOf(
//            R.drawable.monstera,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Bright, indirect",
//        height = "60-90cm",
//        description = "Known for its iconic split leaves, the Monstera Deliciosa or Swiss Cheese Plant is a tropical plant that adds a jungle vibe to any room.",
//        careInstructions = "Water when the top 2 inches of soil are dry. Prefers high humidity. Wipe leaves to keep them dust-free."
//    ),
//    Plant(
//        id = 4,
//        name = "Fiddle Leaf Fig",
//        price = "$75.00",
//        category = "Indoor",
//        images = listOf(
//            R.drawable.fiddleleaf,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Bright, indirect",
//        height = "120-180cm",
//        description = "Ficus lyrata, commonly known as the fiddle-leaf fig, is a popular indoor tree featuring very large, heavily veined, and glossy violin-shaped leaves.",
//        careInstructions = "Prefers stable conditions. Avoid drafts. Water thoroughly when soil is dry to the touch. Do not overwater."
//    ),
//    Plant(
//        id = 5,
//        name = "Pothos",
//        price = "$20.00",
//        category = "Hanging",
//        images = listOf(
//            R.drawable.pothos,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Low to bright, indirect",
//        height = "Vining",
//        description = "Epipremnum aureum, or Pothos, is one of the easiest houseplants to grow. It features heart-shaped leaves and can tolerate a wide range of conditions.",
//        careInstructions = "Very forgiving. Water when the soil feels dry. Can be trimmed to encourage fuller growth."
//    ),
//    Plant(
//        id = 6,
//        name = "ZZ Plant",
//        price = "$40.00",
//        category = "Indoor",
//        images = listOf(
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Low to bright, indirect",
//        height = "60-90cm",
//        description = "The Zamioculcas zamiifolia, or ZZ plant, is known for its wide, dark green, glossy leaves. It is extremely drought-tolerant.",
//        careInstructions = "Water sparingly, allowing soil to dry out completely between waterings. Tolerates neglect well."
//    ),
//    Plant(
//        id = 7,
//        name = "Spider Plant",
//        price = "$25.00",
//        category = "Hanging",
//        images = listOf(
//            R.drawable.spider,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Bright, indirect",
//        height = "30-60cm",
//        description = "Chlorophytum comosum, the spider plant, is an adaptable houseplant and very easy to grow. It produces small plantlets, or 'spiderettes', that dangle down from the mother plant.",
//        careInstructions = "Keep soil slightly moist. Prefers cooler temperatures. Easy to propagate from its plantlets."
//    ),
//    Plant(
//        id = 8,
//        name = "Peace Lily",
//        price = "$35.00",
//        category = "Indoor",
//        images = listOf(
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Medium to low, indirect",
//        height = "45-60cm",
//        description = "Spathiphyllum, or Peace Lily, is known for its beautiful white spathes (flowers) and dark green leaves. It's another excellent air-purifying plant.",
//        careInstructions = "Prefers consistently moist soil. Drooping leaves often indicate it needs water. Keep away from direct sunlight."
//    ),
//    Plant(
//        id = 9,
//        name = "Aloe Vera",
//        price = "$22.00",
//        category = "Succulent",
//        images = listOf(
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Bright, direct",
//        height = "30-60cm",
//        description = "A succulent plant species of the genus Aloe. It is widely known for the healing properties of the gel inside its leaves.",
//        careInstructions = "Requires well-draining soil. Water deeply but infrequently, allowing soil to dry out completely."
//    ),
//    Plant(
//        id = 10,
//        name = "Rubber Plant",
//        price = "$45.00",
//        category = "Indoor",
//        images = listOf(
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Bright, indirect",
//        height = "90-150cm",
//        description = "Ficus elastica, the rubber fig or rubber plant, is a species of plant in the fig genus, native to eastern parts of South Asia and southeast Asia.",
//        careInstructions = "Water when the top inch of soil is dry. Wipe the large leaves with a damp cloth to allow them to breathe."
//    ),
//    Plant(
//        id = 11,
//        name = "Calathea Orbifolia",
//        price = "$50.00",
//        category = "Indoor",
//        images = listOf(
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Medium to bright, indirect",
//        height = "60-80cm",
//        description = "Known for its large, round leaves with beautiful silver stripes. Calatheas are also called 'prayer plants' because their leaves fold up at night.",
//        careInstructions = "Requires high humidity and consistently moist soil. Use distilled or rainwater to avoid leaf browning."
//    ),
//    Plant(
//        id = 12,
//        name = "Bird of Paradise",
//        price = "$65.00",
//        category = "Indoor",
//        images = listOf(
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background
//        ),
//        light = "Bright, direct to indirect",
//        height = "150-180cm",
//        description = "Strelitzia nicolai, the giant white bird of paradise, has large, banana-like leaves and can bring a dramatic, tropical feel indoors.",
//        careInstructions = "Loves bright light. Water generously during the growing season. Can be moved outdoors in summer."
//    )
//)

