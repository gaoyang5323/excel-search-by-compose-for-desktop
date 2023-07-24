class Ktest {

}

fun Test11(age: Int, ceshi: Ktest.(a: Int) -> Int) {

}

interface ByTestI {
    fun show()
}

class ByTestImpl : ByTestI {
    override fun show() {
        print(123)
    }
}

class ByTestImplAndBy(imp: ByTestImpl) : ByTestI by imp {
    fun aa(){
        print("aa")
    }
}

fun main() {
    var byt = ByTestImplAndBy(ByTestImpl());
    byt.show()
    byt.aa()
}