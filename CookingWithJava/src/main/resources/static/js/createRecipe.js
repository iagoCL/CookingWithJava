$(document).ready(function () {
    var ingredientsId = 1;
    var stepId = 1;
    var toolsId = 1;

    var ingredientsNode = $("#ingredients");
    var stepNode = $("#steps");
    var toolsNode = $("#tools");

    var numIngredientsNode = $("#numIngredients");
    var numStepNode = $("#numSteps");
    var numToolsNode = $("#numTools");

    $("#buttonIngredient").click(function () {
        numIngredientsNode.val(++ingredientsId);
        ingredientsNode.append(
            "<div class=\"form-group\" id=\"ingredient-" + ingredientsId + "Group\">" +
            "    <h6 class=\"form-group row col-md-12\">Ingredient " + ingredientsId + ":</h6>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"ingredient-" + ingredientsId + "Name\">Ingredient " + ingredientsId + ": Name</label>" +
            "        <input class=\"col-sm-9\" id=\"ingredient-" + ingredientsId + "Name\" name=\"ingredient-" + ingredientsId + "Name\"" +
            "               type=\"text\"" +
            "               placeholder=\"Ingredient Name" + ingredientsId + "\">" +
            "    </div>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"ingredient-" + ingredientsId + "Amount\">Ingredient " + ingredientsId + ":" +
            "            Amount</label>" +
            "        <input class=\"col-sm-9\" id=\"ingredient-" + ingredientsId + "Amount\" name=\"ingredient-" + ingredientsId + "Amount\"" +
            "               type=\"text\"" +
            "               placeholder=\"Amount Ingredient " + ingredientsId + "\">" +
            "    </div>" +
            "</div>"
        );
    });
    $("#buttonTool").click(function () {
        numToolsNode.val(++toolsId);
        toolsNode.append(
            "<div class=\"form-group\" id=\"tool-" + toolsId + "Group\">" +
            "    <h6 class=\"form-group row col-md-12\">Tool " + toolsId + ":</h6>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"tool-" + toolsId + "Name\">Tool " + toolsId + ": Name</label>" +
            "        <input class=\"col-sm-9\" id=\"tool-" + toolsId + "Name\" name=\"tool-" + toolsId + "Name\"" +
            "               type=\"text\"" +
            "               placeholder=\"Tool Name " + toolsId + "\">" +
            "    </div>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"tool-" + toolsId + "Level\">Tool " + toolsId + ":" +
            "            Difficulty Level</label>" +
            "        <select class=\"col-sm-9\" id=\"tool-" + toolsId + "Level\" name=\"tool-" + toolsId + "Level\">" +
            "            <option value=\"Undefined\">Undefined</option>" +
            "            <option value=\"Easy\">Easy</option>" +
            "            <option value=\"Intermediate\">Intermediate</option>" +
            "            <option value=\"Difficult\">Difficult</option>" +
            "            <option value=\"Expert\">Expert</option>" +
            "            <option value=\"Professional\">Professional</option>" +
            "        </select>" +
            "    </div>" +
            "</div>"
        );
    });

    $("#buttonStep").click(function () {
        numStepNode.val(++stepId);
        stepNode.append(
            "<div class=\"form-group\" id=\"step-" + stepId + "Group\">" +
            "    <h6 class=\"form-group row col-md-12\">Step " + stepId + ":</h6>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"step-" + stepId + "Description\">Step " + stepId + ": Name</label>" +
            "        <textarea class=\"col-sm-9\" id=\"step-" + stepId + "Description\" name=\"step-" + stepId + "Description\"" +
            "                type=\"text\" placeholder=\"Description Step " + stepId + "\"></textarea>" +
            "    </div>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"step-" + stepId + "Duration\">Step " + stepId + ":" +
            "                Duration</label>" +
            "        <input class=\"col-sm-9\" id=\"step-" + stepId + "Duration\" name=\"step-" + stepId + "Duration\"" +
            "                type=\"number\" placeholder=\"Duration Step " + stepId + "\">" +
            "    </div>" +
            "</div>")
    });
    $("#buttonRemoveIngredient").click(function () {
        $("#ingredient-" + ingredientsId + "Group").remove();
        numIngredientsNode.val(--ingredientsId);
    });
    $("#buttonRemoveTool").click(function () {
        $("#tool-" + toolsId + "Group").remove();
        numToolsNode.val(--toolsId);
    });
    $("#buttonRemoveStep").click(function () {
        $("#step-" + stepId + "Group").remove();
        numStepNode.val(--stepId);
    });
});
