$(document).ready(function (){
    $('table #editButton').on('click',function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (category, status){
            $('#idEdit').val(category.id);
            $('#nameEdit').val(category.nom);
        });
        $('#editModal').modal();
    });
});

// $(document).ready(function (){
//     $('#editButton').on('click', function (event){
//         event.preventDefault();
//         var href = $(this).attr('href');
//         $.get(href, function (category, status){
//             if(category && category.id && category.nom) {
//                 $('#idEdit').val(category.id);
//                 $('#nameEdit').val(category.nom);
//                 $('#editModal').modal();
//             }
//         });
//     });
// });