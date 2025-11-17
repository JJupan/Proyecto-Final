const API_URL = "http://localhost:8080/students";

let studentCache = [];

document.addEventListener("DOMContentLoaded", loadStudents);


function loadStudents() {
    fetch(API_URL)
        .then(res => res.json())
        .then(data => {
            studentCache = data;
            renderTable(studentCache);
        })
        .catch(() => Swal.fire("Error", "No se pudieron cargar los estudiantes", "error"));
}


function renderTable(students) {
    const tbody = document.getElementById("studentsTableBody");
    tbody.innerHTML = "";

    students.forEach(s => {
        tbody.innerHTML += `
            <tr>
                <td>${s.id}</td>
                <td>${s.nombre}</td>
                <td>${s.correo}</td>
                <td>${s.numeroTelefono}</td>
                <td>${s.idioma}</td>
                <td>
                    <button class="btn btn-sm btn-warning" onclick="editStudent(${s.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="deleteStudent(${s.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `;
    });
}


function openCreateModal() {
    document.getElementById("studentForm").reset();
    document.getElementById("studentId").value = "";
    $("#studentModal").modal("show");
}


document.getElementById("studentForm").addEventListener("submit", e => {
    e.preventDefault();

    const id = document.getElementById("studentId").value;

    const student = {
        nombre: document.getElementById("nombre").value,
        correo: document.getElementById("correo").value,
        numeroTelefono: document.getElementById("numero_telefono").value,
        idioma: document.getElementById("idioma").value
    };

    id ? updateStudent(id, student) : createStudent(student);
});


function createStudent(student) {
    fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(student)
    })
        .then(res => {
            if (!res.ok) throw new Error();
            return res.json();
        })
        .then(() => {
            $("#studentModal").modal("hide");
            Swal.fire("Éxito", "Estudiante creado correctamente", "success");
            loadStudents();
        })
        .catch(() => Swal.fire("Error", "Correo ya existente o datos inválidos", "error"));
}


function editStudent(id) {
    fetch(`${API_URL}/${id}`)
        .then(res => res.json())
        .then(s => {
            document.getElementById("studentId").value = s.id;
            document.getElementById("nombre").value = s.nombre;
            document.getElementById("correo").value = s.correo;
            document.getElementById("numero_telefono").value = s.numeroTelefono;
            document.getElementById("idioma").value = s.idioma;

            $("#studentModal").modal("show");
        });
}


function updateStudent(id, student) {
    fetch(`${API_URL}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(student)
    })
        .then(res => {
            if (!res.ok) throw new Error();
            return res.json();
        })
        .then(() => {
            $("#studentModal").modal("hide");
            Swal.fire("Éxito", "Estudiante actualizado", "success");
            loadStudents();
        })
        .catch(() => Swal.fire("Error", "Correo duplicado o datos inválidos", "error"));
}


function deleteStudent(id) {
    Swal.fire({
        title: "¿Eliminar?",
        text: "Esta acción no se puede deshacer",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Eliminar"
    }).then(result => {
        if (result.isConfirmed) {
            fetch(`${API_URL}/${id}`, { method: "DELETE" })
                .then(res => {
                    if (!res.ok) throw new Error();
                    Swal.fire("Eliminado", "Estudiante borrado", "success");
                    loadStudents();
                })
                .catch(() => Swal.fire("Error", "No se pudo eliminar", "error"));
        }
    });
}


document.getElementById("searchInput").addEventListener("input", function () {
    const query = this.value.toLowerCase();

    const filtered = studentCache.filter(s =>
        s.nombre.toLowerCase().includes(query) ||
        s.correo.toLowerCase().includes(query) ||
        s.numeroTelefono.toLowerCase().includes(query) ||
        s.idioma.toLowerCase().includes(query)
    );

    renderTable(filtered);
});
