import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.util.*

class MyApp : App(MyView::class)

class MyView(
        private var operators: Array<String> = arrayOf("+", "-", "/", "*", "(", ")"),
        private var stack: Stack<String> = Stack(),
        override val root: Form = Form(),
        private var isLastCharacterOp: Boolean = true, private val value: SimpleStringProperty = SimpleStringProperty("0")) : View() {

    private fun isOp(str: String): Boolean {
        return operators.contains(str)
    }

    private fun getPriorityOfOperator(str: String): Int {
        return when (str) {
            "(" -> 4
            ")" -> 4
            "^" -> 3
            "*" -> 2
            "/" -> 2
            "+" -> 1
            "-" -> 1
            "=" -> 0
            else -> {
                0
            }
        }
    }

    private fun lastOperator(): String {
        return stack.lastOrNull { x -> isOp(x) } ?: return "ERROR"
    }

    private fun onOpButtonPressed(text: String) {
        isLastCharacterOp = true
        if (stack.lastIndex > -1 && stack[stack.lastIndex] == value.get()) {
        } else
            stack.add(value.get())

        if (getPriorityOfOperator(text) <= getPriorityOfOperator(lastOperator())) {
            while (stack.size > 1 && lastOperator() != "(")
                if (calc()) {
                    value.set(stack.last())
                }

        }
        if (text != "=")
            stack.add(text)
    }

    private fun calc(): Boolean {
        if (stack.size < 3) return false

        val secondArgument = stack.pop()
        val op = stack.pop()
        val firstArgument = stack.pop()

        when (op) {
            "+" -> stack.add((firstArgument.toDouble() + secondArgument.toDouble()).toString())
            "-" -> stack.add((firstArgument.toDouble() - secondArgument.toDouble()).toString())
            "*" -> stack.add((firstArgument.toDouble() * secondArgument.toDouble()).toString())
            "/" -> stack.add((firstArgument.toDouble() / secondArgument.toDouble()).toString())
            else -> return false
        }
        return true
    }

    private fun clear() {
        stack.clear()
        value.set("0")
        isLastCharacterOp = true
    }


    private fun onDot() {
        if (!isLastCharacterOp && !value.get().contains(".")) {
            onNumberButtonPressed(".")
        }
    }

    private fun onClosingParenthesis() {
        if (stack.contains("(")) {
            if (stack.lastIndex > -1 && stack[stack.lastIndex] == value.get()) {
            } else
                stack.add(value.get())
            while (stack.size > 1 && lastOperator() != "(")
                if (calc()) {
                    value.set(stack.last())
                }
            val lastIndex = stack.lastIndex
            if (stack[lastIndex - 1] == "(") {
                stack.removeElementAt(lastIndex - 1)
            }


        } else {
            clear()
        }
    }

    private fun onOpeningParanthesis(text: String) {
        if (stack.lastIndex > -1 && value.get() != stack[stack.lastIndex] && !isOp(stack[stack.lastIndex])) {
            clear()
        } else {
            if (stack.lastIndex == -1 && value.get() != "0") {
            }
            else {
                if (stack.size == 0 || !isOp(stack[stack.lastIndex]) || stack[stack.lastIndex - 1] == value.get()) {
                    stack.add(text)
                    value.set("0")
                    isLastCharacterOp = true
                } else {
                    clear()
                }
            }
        }
    }

    private fun onNumberButtonPressed(str: String) {
        when {
            isLastCharacterOp -> value.set(str)
            else -> if (value.get() == "0") {
                value.set(str)
            } else {
                value.set(value.get() + str)
            }
        }
        isLastCharacterOp = false
    }


    var keypad = vbox {
        vbox {
            label {
            }.bind(value)
        }
        vbox {
            hbox {
                button("7") {
                    //                    height = 20.0;
                    action {
                        onNumberButtonPressed("7")
                    }

                }
                button("8") {

                    action {
                        onNumberButtonPressed("8")
                    }
                }
                button("9") {
                    action {
                        onNumberButtonPressed("9")
                        isOp("9")
                    }
                }
                button("/") {
                    action {
                        onOpButtonPressed(this.text)
                    }
                }
            }
            hbox {
                button("4") {
                    action {
                        onNumberButtonPressed("4")
                    }
                }
                button("5") {
                    action {
                        onNumberButtonPressed("5")
                    }
                }
                button("6") {
                    action {
                        onNumberButtonPressed("6")
                    }
                }
                button("*") {
                    action {
                        onOpButtonPressed(this.text)
                    }
                }
            }
            hbox {
                button("1") {
                    action {
                        onNumberButtonPressed("1")
                    }
                }
                button("2") {
                    action {
                        onNumberButtonPressed("2")
                    }
                }
                button("3") {
                    action {
                        onNumberButtonPressed("3")
                    }
                }
                button("-") {
                    action {
                        onOpButtonPressed(this.text)
                    }
                }
            }
            hbox {
                button("0") {
                    action {
                        onNumberButtonPressed("0")
                    }
                }
                button(".") {
                    action {
                        onDot()
                    }
                }
                button("+") {
                    action {
                        onOpButtonPressed(this.text)
                    }
                }
                button("=") {

                    action {
                        onOpButtonPressed("=")
                    }
                }

            }
            hbox {
                button("(") {
                    action {
                        onOpeningParanthesis(this.text)
                    }
                }
                button(")") {
                    action {
                        onClosingParenthesis()
                    }
                }
                button("C") {
                    action {
                        this@MyView.clear()
                    }
                }
            }
        }
    }

}


fun main(args: Array<String>) {
    Application.launch(MyApp::class.java, *args)
}


