package client.exceptions

class InvalidUser : NullPointerException ("Invalid user")
class NoteNotFound : NullPointerException ("Note not found")
class BadRequest: RuntimeException ("Bad request")
class CommentNotFound: NullPointerException("Comment not found")
class AccessDenied: RuntimeException("Access denied")
class CommentAlreadyDeleted: RuntimeException("Comment already deleted")
class CommentIsNotDeleted: RuntimeException("Comment is not deleted")