const apiUrl = 'http://localhost:8080/api/v1/student';

// Fetch and display students
function fetchStudents() {
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('studentTable').querySelector('tbody');
            tableBody.innerHTML = ''; // Clear the table before adding new rows
            data.forEach(student => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${student.id}</td>
                    <td>${student.name}</td>
                    <td>${student.email}</td>
                    <td>${student.dob}</td>
                    <td>${student.age}</td>
                    <td>
                        <button onclick="deleteStudent(${student.id})">Delete</button>
                        <button onclick="prepareUpdateStudent(${student.id}, '${student.name}', '${student.email}')">Update</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching students:', error));
}

// Add a new student
document.getElementById('addStudentForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;

    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, email })
    })
    .then(response => {
        if (response.ok) {
            fetchStudents(); // Refresh student list
            document.getElementById('name').value = '';
            document.getElementById('email').value = '';
        } else {
            alert('Failed to add student');
        }
    })
    .catch(error => console.error('Error adding student:', error));
});

// Delete a student
function deleteStudent(id) {
    fetch(`${apiUrl}/${id}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            fetchStudents(); // Refresh student list
        } else {
            alert('Failed to delete student');
        }
    })
    .catch(error => console.error('Error deleting student:', error));
}

// Prepare student update (open form for updating)
function prepareUpdateStudent(id, currentName, currentEmail) {
    const name = prompt("Enter new name:", currentName);
    const email = prompt("Enter new email:", currentEmail);
    if (name && email) {
        updateStudent(id, name, email);
    }
}

// Update student
function updateStudent(id, name, email) {
    fetch(`${apiUrl}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, email })
    })
    .then(response => {
        if (response.ok) {
            fetchStudents(); // Refresh student list
        } else {
            alert('Failed to update student');
        }
    })
    .catch(error => console.error('Error updating student:', error));
}

// Fetch students when the page loads
fetchStudents();
