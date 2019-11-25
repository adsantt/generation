$(document).ready(function() {
    $('.carousel').carousel()
    $('#content').load('./home.html'); //pagina inicial
    $('ul#nav li a').click(function() {
        let page = $(this).attr('href');
        $('#content').load('./' + page + '.html');
        return false;
    });
    $('#txtSearch').keypress(function(event) {
        let keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            buscarMonumento();
        }
    });
    $('#btnSearch').click(function() {
        buscarMonumento();
    });
    /*('#btnFile').click(function(){
        console.log("hola");
        processImage();
    });*/
    
});
//Metodo para cargar imagenes del carrusel
function monumentosHome() {
    let request = new XMLHttpRequest();
    request.onreadystatechange = function() {

        if (this.readyState == 4) {
            mostrarMonumentos(this.responseText);
        }
    };
    let url = "http://localhost:8080/ApiMonumentos/api/monumentos/populares/6";
    request.open("GET", url, true);
    request.send();
}


function mostrarMonumentos(data) {
    let namesHome = [];
    let imgsHome = [];
    let monumentos = JSON.parse(data);
    let list = monumentos['data'];
    for (let i = 0; i < list.length; i++) {
        namesHome[i] = list[i].nombre_construccion;
        imgsHome[i] = list[i].foto;
    }
    let img1, img2, img3, img4, img5, img6;
    img1 = document.getElementById("img1");
    img1.innerHTML = "<img src=" + imgsHome[0] + "><div class=\"carousel-caption\"><h3>" + namesHome[0] + "</div>";

    img2 = document.getElementById("img2");
    img2.innerHTML = "<img src=" + imgsHome[1] + "><div class=\"carousel-caption\"><h3>" + namesHome[1] + "</div>";

    img3 = document.getElementById("img3");
    img3.innerHTML = "<img src=" + imgsHome[2] + "><div class=\"carousel-caption\"><h3>" + namesHome[2] + "</div>";

    img4 = document.getElementById("img4");
    img4.innerHTML = "<img src=" + imgsHome[3] + "><div class=\"carousel-caption\"><h3>" + namesHome[3] + "</div>";

    img5 = document.getElementById("img5");
    img5.innerHTML = "<img src=" + imgsHome[4] + "><div class=\"carousel-caption\"><h3>" + namesHome[4] + "</div>";

    img6 = document.getElementById("img6");
    img6.innerHTML = "<img src=" + imgsHome[5] + "><div class=\"carousel-caption\"><h3>" + namesHome[5] + "</div>";

}


function processImage() {

    let imageSelected = document.getElementById("inputImage").files;
    if (imageSelected.length > 0) {
        let fileToLoad = imageSelected[0];

        let fileReader = new FileReader();
        let srcData;
        fileReader.onload = function(fileLoadedEvent) {
            srcData = fileLoadedEvent.target.result; // <--- data: base64
            sendImage(srcData);
        };
        fileReader.readAsDataURL(fileToLoad);
    }
}

function sendImage(data) {

    let request = new XMLHttpRequest();
    let base64 = { img: data };
    let img = JSON.stringify(base64);

    let url = "http://localhost:8080/ApiMonumentos/api/monumentos/foto";
    request.open("POST", url, true);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.onreadystatechange = function() {
        if (this.readyState == 4) {
            let json = JSON.parse(request.responseText);
            crearMonumento(json);
        }
    };
    request.send(img);
    $("#content").load("./monumento.html");
}
function buscarMonumento(){
    txtSearch = document.getElementById("txtSearch");
    let query = txtSearch.value;
    consultarMonumento(query);
}

function consultarMonumento(query) {
    //Se utilizará tanto en busqueda de texto como en busqueda al hacer click en las imagenes

    let request = new XMLHttpRequest;
    request.onreadystatechange = function() {
        if (this.readyState == 4) {
            json = JSON.parse(request.responseText);
            crearMonumento(json);
        }
    };
    let url = "http://localhost:8080/ApiMonumentos/api/monumentos/" + query;
    request.open("GET", url, true);
    request.send();
    $('#content').load('./monumento.html');
}

function crearMonumento(data) {

    let monumentosList = data['data'];
    console.log(monumentosList);
    let nombre = monumentosList['nombre_construccion'];
    let fechaConstruccion = monumentosList['fecha_construccion'];
    let arquitectura = monumentosList['tipo_arquitectura'];
    let arquitecto = monumentosList['arquitecto'];
    let tiempoConstruccion = monumentosList['duracion_construccion'];
    let restauraciones = monumentosList['contiene_restauraciones'];
    let uso = monumentosList['uso_edificio'];
    let informacion = monumentosList['informacion'];
    let leyendas = monumentosList['leyendas'];
    let foto = monumentosList['foto'];

    document.getElementById("construccion").innerHTML = nombre;
    document.getElementById("foto").innerHTML = "<img class=\"card-img-top\" src=" + foto + ">";
    document.getElementById("fecha-construccion").innerHTML = fechaConstruccion;
    document.getElementById("arquitecto").innerHTML = arquitecto;
    document.getElementById("estilo").innerHTML = arquitectura;
    document.getElementById("informacion").innerHTML = informacion;;
    document.getElementById("duracion").innerHTML = tiempoConstruccion;
    document.getElementById("usos").innerHTML = uso;
    document.getElementById("leyendas").innerHTML = leyendas;
}



//Las siguientes funciones son para el autocompletado
function buscarNombre() {
    let txtNombre = document.getElementById("txtSearch");
    let value = txtNombre.value;
    let request = new XMLHttpRequest;
    request.onreadystatechange = function() {
        if (this.readyState == 4) {
            getList(request.responseText);
        }
    };
    let url = "http://localhost:8080/ApiMonumentos/api/monumentos/buscar?nombre=" + value;
    request.open("GET", url, true);
    request.send();
}


function getList(data) {
    console.log(data);
    let namesList = [];
    let list = JSON.parse(data);

    for (let i in list) {
        namesList += list.data[i];
    }
    $(".auto").autoComplete({
        resolverSettings: {
            url: 'namesList'
        }
    });
}