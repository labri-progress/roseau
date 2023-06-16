// Top-level classes are either public or package-private
public class TopLevelClassPublic {
	public static String AString;
	// Inner classes
	class InnerClass {}
	public class InnerClassPublic {
		public int number;
		public String text;

		public InnerClassPublic(int number, String text) {
			this.number = number;
			this.text = text;
		}

		public static int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public String setText(String text) {
			this.text = text;
		}
	}
	protected class InnerClassProtected {}
	private class InnerClassPrivate {}

	// Nested classes
	static class NestedClass {}
	public static class NestedClassPublic {}
	protected static class NestedClassProtected {}
	private static class NestedClassPrivate {}

	// Nested interfaces are implicitly static (no inner interfaces)
	interface NestedInterface {}
	public interface NestedInterfacePublic {}
	protected interface NestedInterfaceProtected {}
	private interface NestedInterfacePrivate {}

	// Nested records are implicitly static (no inner records)
	record NestedRecord() {}
	public record NestedRecordPublic() {}
	protected record NestedRecordProtected() {}
	private record NestedRecordPrivate() {}

	// Nested enums are implicitly static (no inner enums)
	enum NestedEnum {}
	public enum NestedEnumPublic {}
	protected enum NestedEnumProtected {}
	private enum NestedEnumPrivate {}

	// Annotations are interfaces so nested annotations are implicitly static (no inner annotations)
	@interface NestedAnnotation {}
	public @interface NestedAnnotationPublic {}
	protected @interface NestedAnnotationProtected {}
	private @interface NestedAnnotationPrivate {}
}
