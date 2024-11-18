package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  private lateinit var studentAdapter: StudentAdapter
  private var deletedStudent: StudentModel? = null
  private var deletedPosition: Int? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val students = mutableListOf(
      StudentModel("Nguyễn Văn An", "SV001"),
      StudentModel("Trần Thị Bảo", "SV002"),
      StudentModel("Lê Hoàng Cường", "SV003"),
      StudentModel("Phạm Thị Dung", "SV004"),
      StudentModel("Đỗ Minh Đức", "SV005"),
      StudentModel("Vũ Thị Hoa", "SV006"),
      StudentModel("Hoàng Văn Hải", "SV007"),
    )

    studentAdapter = StudentAdapter(students, { student, position ->
      // Xử lý chỉnh sửa sinh viên
      showEditStudentDialog(student, position, students, studentAdapter)
    }, { position ->
      // Xử lý xóa sinh viên
      showDeleteConfirmationDialog(position, students, studentAdapter)
    })

    findViewById<RecyclerView>(R.id.recycler_view_students).apply {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentDialog(students, studentAdapter)
    }
  }

  // Hàm thêm sinh viên
  private fun showAddStudentDialog(students: MutableList<StudentModel>, adapter: StudentAdapter) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
    val dialog = AlertDialog.Builder(this)
      .setTitle("Thêm sinh viên")
      .setView(dialogView)
      .create()

    dialogView.findViewById<Button>(R.id.button_add).setOnClickListener {
      val name = dialogView.findViewById<EditText>(R.id.edit_text_student_name).text.toString()
      val id = dialogView.findViewById<EditText>(R.id.edit_text_student_id).text.toString()

      if (name.isNotBlank() && id.isNotBlank()) {
        val newStudent = StudentModel(name, id)
        adapter.addStudentToTop(newStudent)
        dialog.dismiss()
      }
    }

    dialog.show()
  }

  // Hàm chỉnh sửa sinh viên
  private fun showEditStudentDialog(student: StudentModel, position: Int, students: MutableList<StudentModel>, adapter: StudentAdapter) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
    val dialog = AlertDialog.Builder(this)
      .setTitle("Edit")
      .setView(dialogView)
      .create()

    val editName = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
    val editId = dialogView.findViewById<EditText>(R.id.edit_text_student_id)
    editName.setText(student.studentName)
    editId.setText(student.studentId)

    dialogView.findViewById<Button>(R.id.button_add).apply {
      text = "Update"
      setOnClickListener {
        val newName = editName.text.toString()
        val newId = editId.text.toString()

        if (newName.isNotBlank() && newId.isNotBlank()) {
          students[position] = StudentModel(newName, newId)
          adapter.notifyItemChanged(position)
          dialog.dismiss()
        }
      }
    }

    dialog.show()
  }

  // Xác nhận xóa sinh viên
  private fun showDeleteConfirmationDialog(position: Int, students: MutableList<StudentModel>, adapter: StudentAdapter) {
    val student = students[position]
    val dialog = AlertDialog.Builder(this)
      .setTitle("Delete")
      .setMessage("Are you sure deleting ${student.studentName}?")
      .setPositiveButton("Yes") { _, _ ->
        deletedStudent = students[position] // Lưu lại thông tin sinh viên bị xóa
        deletedPosition = position
        students.removeAt(position)
        adapter.notifyItemRemoved(position)
        showUndoSnackbar(position, students, adapter)
      }
      .setNegativeButton("No", null)
      .create()

    dialog.show()
  }

  // Hiển thị Snackbar với Undo
  private fun showUndoSnackbar(position: Int, students: MutableList<StudentModel>, adapter: StudentAdapter) {
    val snackbar = Snackbar.make(findViewById(R.id.main), "Student deleted", Snackbar.LENGTH_LONG)
    snackbar.setAction("Undo") {
      // Khôi phục sinh viên đã xóa
      deletedStudent?.let { student ->
        students.add(position, student)
        adapter.notifyItemInserted(position)
      }
    }
    snackbar.show()
  }
}
