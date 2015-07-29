package org.fenixedu.academic.domain.onlineTests;

import java.nio.charset.StandardCharsets;

import org.fenixedu.bennu.core.domain.User;

class QuestionFile extends QuestionFile_Base {

    QuestionFile(Question question, String fileName, String xmlContent) {
        super();
        setQuestion(question);
        init(fileName, fileName, xmlContent.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean isAccessible(User user) {
        // Question Files must never be accessible
        return false;
    }

    @Override
    public void delete() {
        setQuestion(null);
        super.delete();
    }

}
