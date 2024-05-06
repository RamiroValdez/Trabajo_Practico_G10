document.addEventListener('DOMContentLoaded', function() {
    var tipoMovimiento = document.getElementById('tipoMovimiento');
    var categoriaMovimiento = document.getElementById('categoriaMovimiento');

    tipoMovimiento.addEventListener('change', function() {
        // Limpiar las opciones existentes
        categoriaMovimiento.innerHTML = '';

        var categorias = [];
        if (this.value === 'INGRESO') {
            categorias = ['SUELDO', 'REGALO', 'REEMBOLSO', 'EXTRA'];
        } else if (this.value === 'EGRESO') {
            categorias = ['RESTAURANTE', 'ENTRETENIMIENTO', 'SALUD_BELLEZA', 'INDUMENTARIA', 'TRANSPORTE', 'PAGO_DE_SERVICIOS', 'SUPERMERCADO'];
        }

        // Generar nuevas opciones
        for (var i = 0; i < categorias.length; i++) {
            var option = document.createElement('option');
            option.value = categorias[i];
            option.text = categorias[i];
            categoriaMovimiento.add(option);
        }
    });
});