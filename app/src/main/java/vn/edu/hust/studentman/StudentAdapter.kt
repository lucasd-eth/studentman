package vn.edu.hust.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
  val students: MutableList<StudentModel>,
  private val onEditClick: (StudentModel, Int) -> Unit,
  private val onRemoveClick: (Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context)
      .inflate(R.layout.layout_student_item, parent, false)
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId

    // Xử lý sự kiện nhấn vào nút Edit
    holder.imageEdit.setOnClickListener {
      onEditClick(student, position) // Gọi lại hàm khi nhấn vào nút Edit
    }

    // Xử lý sự kiện nhấn vào nút Remove
    holder.imageRemove.setOnClickListener {
      onRemoveClick(position) // Gọi lại hàm khi nhấn vào nút Remove
    }
  }

  // Phương thức thêm sinh viên vào đầu danh sách
  fun addStudentToTop(student: StudentModel) {
    students.add(0, student) // Thêm sinh viên vào đầu danh sách
    notifyItemInserted(0)
  }
}
