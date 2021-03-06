/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 1/Ago/2003
 */

package org.fenixedu.academic.service.services.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.fenixedu.academic.domain.onlineTests.DistributedTest;
import org.fenixedu.academic.domain.onlineTests.Question;
import org.fenixedu.academic.domain.onlineTests.StudentTestQuestion;
import org.fenixedu.academic.domain.onlineTests.utils.ParseSubQuestion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.InfoStudent;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.utils.ParseQuestionException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class AddStudentsToDistributedTest {

    protected void run(String executionCourseId, String distributedTestId, List<InfoStudent> infoStudentList)
            throws InvalidArgumentsServiceException {
        if (infoStudentList == null || infoStudentList.size() == 0) {
            return;
        }
        DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        Set<StudentTestQuestion> studentTestQuestions =
                distributedTest.findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder();
        int order = 0;
        for (StudentTestQuestion studentTestQuestionExample : studentTestQuestions) {
            if (studentTestQuestionExample.getQuestion().getSubQuestions() == null
                    || studentTestQuestionExample.getQuestion().getSubQuestions().size() == 0) {
                try {
                    new ParseSubQuestion().parseSubQuestion(studentTestQuestionExample.getQuestion());
                } catch (ParseQuestionException e) {
                    throw new InvalidArgumentsServiceException();
                }
            }

            if (!studentTestQuestionExample.isSubQuestion()) {
                order++;
                List<Question> questionList = new ArrayList<Question>();
                questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());

                for (InfoStudent infoStudent : infoStudentList) {
                    Registration registration = FenixFramework.getDomainObject(infoStudent.getExternalId());
                    StudentTestQuestion studentTestQuestion = new StudentTestQuestion();
                    studentTestQuestion.setStudent(registration);
                    studentTestQuestion.setDistributedTest(distributedTest);
                    studentTestQuestion.setTestQuestionOrder(order);
                    studentTestQuestion.setTestQuestionValue(studentTestQuestionExample.getTestQuestionValue());
                    studentTestQuestion.setCorrectionFormula(studentTestQuestionExample.getCorrectionFormula());
                    studentTestQuestion.setTestQuestionMark(new Double(0));
                    studentTestQuestion.setResponse(null);

                    if (questionList.size() == 0) {
                        questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());
                    }
                    Question question = null;
                    try {
                        question = getStudentQuestion(questionList);
                    } catch (ParseQuestionException e) {
                        throw new InvalidArgumentsServiceException();
                    }
                    if (question == null) {
                        throw new InvalidArgumentsServiceException();
                    }
                    if (question.getSubQuestions().size() >= 1
                            && question.getSubQuestions().iterator().next().getItemId() != null) {
                        studentTestQuestion.setItemId(question.getSubQuestions().iterator().next().getItemId());
                    }
                    studentTestQuestion.setQuestion(question);
                    questionList.remove(question);
                }
            }
        }
    }

    private Question getStudentQuestion(List<Question> questions) throws ParseQuestionException {
        Question question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question.getSubQuestions() == null || question.getSubQuestions().size() == 0 ? new ParseSubQuestion()
                .parseSubQuestion(question) : question;
    }

    // Service Invokers migrated from Berserk

    private static final AddStudentsToDistributedTest serviceInstance = new AddStudentsToDistributedTest();

    @Atomic
    public static void runAddStudentsToDistributedTest(String executionCourseId, String distributedTestId,
            List<InfoStudent> infoStudentList) throws InvalidArgumentsServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, distributedTestId, infoStudentList);
    }

}