/*
AbbreviationAsWordInName
allowedAbbreviationLength = 0
allowedAbbreviations = NUMBER, MARAZMATIC, VARIABLE , UID, VALUE , A, O, ID
ignoreFinal = false
ignoreStatic = false
ignoreStaticFinal = false
ignoreOverriddenMethods = (default)true
tokens = CLASS_DEF, VARIABLE_DEF, METHOD_DEF, ENUM_DEF, ENUM_CONSTANT_DEF, PARAMETER_DEF, \
         INTERFACE_DEF, ANNOTATION_DEF


*/

package com.puppycrawl.tools.checkstyle.checks.naming.abbreviationaswordinname;

public class InputAbbreviationAsWordInNameLengthOneWords {

    abstract class OAuth { // case from issue # 10889: not a violation because O is in allowedAbbreviations
    }

    abstract class NonAbstractAClassName { // not a violation because A is in allowedAbbreviations
    }

    abstract class FactoryAWithBadName { // not a violation because A is in allowedAbbreviations
    }

    abstract class AbstractClassName {
        abstract class NonAbstractInnerClass {
        }
    }

    abstract class ClassFactory1 {
        abstract class WellNamedFactory {
        }
    }

    class NonAbstractClass1 {
    }

    class AbstractClass1 {
    }

    class Class1Factory1 {
    }

    abstract class AbstractClassName3 {
        class AbstractBInnerClass { // violation
        }
    }

    abstract class Class3Factory {
        class WellNamedAAAFactory { // violation
            public void marazmaticMethoDName() { // violation
                int marazmaticVARIABLEName = 2;
                int MARAZMATICVariableName = 1;
            }
        }
    }

    interface Directions {
      int right=1;
      int left=2;
      int up=3;
      int down=4;
    }

    interface BadNameForInterface
    {
       void interfaceMethod();
    }

    abstract static class NonAaaAbstractClassName2 {
        public int serialNUMBER = 6;
        public final int s1erialNUMBER = 6;
        private static int s2erialNUMBER = 6;
        private static final int s3erialNUMBER = 6;
    }

    interface Interface1 {

        String VALUEEEE = "value"; // violation

    }

    interface Interface2 {

        static String VALUEEEE = "value"; // violation

    }

    interface Interface3 {

        final String VALUEEEE = "value"; // violation

    }

    interface Interface4 {

        final static String VALUEEEE = "value"; // violation

    }

    class FileNameFormatException extends Exception {

        private static final long serialVersionUID = 1L;

        public FileNameFormatException(Exception e) {
            super(e);
        }
    }

    class StateX {
        int userID;
        int scaleX, scaleY, scaleZ;

        int getScaleX() {
            return this.scaleX;
        }
    }

    @interface Annotation1 {
        String VALUE = "value"; // in @interface this is final/static
    }

    @interface Annotation2 {
        static String VALUE = "value"; // in @interface this is final/static
    }

    @interface Annotation3 {
        final String VALUE = "value"; // in @interface this is final/static
    }

    @interface Annotation4 {
        final static String VALUE = "value"; // in @interface this is final/static
    }

    final class InnerClassOneVIOLATION { // violation
        // only variable definitions are affected by ignore static/final properties
    }

    static class InnerClassTwoVIOLATION { // violation
        // only variable definitions are affected by ignore static/final properties
    }

    static final class InnerClassThreeVIOLATION { // violation
        // only variable definitions are affected by ignore static/final properties
    }

}
