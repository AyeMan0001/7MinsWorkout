package eu.tutorials.a7minutesworkout
//here we set our exercises data
//this is the clean way to do it
//make your variables private
//but make public functions to get them
class ExerciseModel(
    private var id:Int,
    private var name:String,
    private var image:Int,
    private var isCompleted:Boolean,
    private var isSelected:Boolean
) {
    //now we are setting the getter and setter
    fun getId(): Int{
        return id
    }
    fun setId(id:Int){
        this.id = id
    }
    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getImage(): Int {
        return image
    }

    fun setImage(image: Int) {
        this.image = image
    }

    fun getIsCompleted(): Boolean {
        return isCompleted
    }

    fun setIsCompleted(isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }

    fun getIsSelected(): Boolean {
        return isSelected
    }

    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }

}
