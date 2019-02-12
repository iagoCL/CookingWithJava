$(document).ready(function () {
    var ingredientesId = 1;
    var pasoId = 1;
    var utensiliosId = 1;

    var ingredientesNode = $("#ingredientes");
    var pasoNode = $("#pasos");
    var utensiliosNode = $("#utensilios");

    $("#buttonIngrediente").click(function () {
        ++ingredientesId;
        ingredientesNode.append(
            "<div class=\"form-group\" id=\"ingrediente-" + ingredientesId + "Group\">" +
            "    <h6 class=\"form-group row col-md-12\">Ingrediente " + ingredientesId + ":</h6>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"ingrediente-" + ingredientesId + "Name\">Ingrediente " + ingredientesId + ": Nombre</label>" +
            "        <input class=\"col-sm-9\" id=\"ingrediente-" + ingredientesId + "Name\" name=\"ingrediente-" + ingredientesId + "Name\"" +
            "               type=\"text\"" +
            "               placeholder=\"Nombre Ingrediente " + ingredientesId + "\">" +
            "    </div>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"ingrediente-" + ingredientesId + "Cantidad\">Ingrediente " + ingredientesId + ":" +
            "            Cantidad</label>" +
            "        <input class=\"col-sm-9\" id=\"ingrediente-" + ingredientesId + "Cantidad\" name=\"ingrediente-" + ingredientesId + "Cantidad\"" +
            "               type=\"text\"" +
            "               placeholder=\"Cantidad Ingrediente " + ingredientesId + "\">" +
            "    </div>" +
            "</div>"
        );
    });
    $("#buttonUtensilio").click(function () {
        ++utensiliosId;
        utensiliosNode.append(
            "<div class=\"form-group\" id=\"utensilio-" + utensiliosId + "Group\">" +
            "    <h6 class=\"form-group row col-md-12\">Utensilio " + utensiliosId + ":</h6>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"utensilio-" + utensiliosId + "Name\">Utensilio " + utensiliosId + ": Nombre</label>" +
            "        <input class=\"col-sm-9\" id=\"utensilio-" + utensiliosId + "Name\" name=\"utensilio-" + utensiliosId + "Name\"" +
            "               type=\"text\"" +
            "               placeholder=\"Nombre Utensilio " + utensiliosId + "\">" +
            "    </div>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"utensilio-" + utensiliosId + "Nivel\">Utensilio " + utensiliosId + ":" +
            "            Nivel de dificultad</label>" +
            "        <select class=\"col-sm-9\" id=\"utensilio-" + utensiliosId + "Nivel\" name=\"utensilio-" + utensiliosId + "Nivel\">" +
            "            <option value=\"Indefinido\">Indefinido</option>" +
            "            <option value=\"Facil\">Facil</option>" +
            "            <option value=\"Medio\">Medio</option>" +
            "            <option value=\"Dificil\">Dificil</option>" +
            "            <option value=\"Experto\">Experto</option>" +
            "            <option value=\"Profesional\">Profesional</option>" +
            "        </select>" +
            "    </div>" +
            "</div>"
        );
    });

    $("#buttonPaso").click(function () {
        ++pasoId;
        pasoNode.append(
            "<div class=\"form-group\" id=\"paso-" + pasoId + "Group\">" +
            "    <h6 class=\"form-group row col-md-12\">Paso " + pasoId + ":</h6>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"paso-" + pasoId + "Descripcion\">Paso " + pasoId + ": Nombre</label>" +
            "        <textarea class=\"col-sm-9\" id=\"paso-" + pasoId + "Descripcion\" name=\"paso-" + pasoId + "Descripcion\"" +
            "                type=\"text\" placeholder=\"Descripcion Paso " + pasoId + "\"></textarea>" +
            "    </div>" +
            "    <div class=\"form-group row\">" +
            "        <label class=\"col-sm-3\" for=\"paso-" + pasoId + "Duracion\">Paso " + pasoId + ":" +
            "                Duración</label>" +
            "        <input class=\"col-sm-9\" id=\"paso-" + pasoId + "Duracion\" name=\"paso-" + pasoId + "Duracion\"" +
            "                type=\"number\" placeholder=\"Duracion Paso " + pasoId + "\">" +
            "    </div>" +
            "</div>")
    });
    $("#buttonQuitarIngrediente").click(function () {
        $("#ingrediente-" + ingredientesId + "Group").remove();
        --ingredientesId;
    });
    $("#buttonQuitarUtensilio").click(function () {
        $("#utensilio-" + utensiliosId + "Group").remove();
        --utensiliosId;
    });
    $("#buttonQuitarPaso").click(function () {
        $("#paso-" + pasoId + "Group").remove();
        --pasoId;
    });
});
