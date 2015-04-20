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
 * Created on 23/Set/2003
 * 
 */
package org.fenixedu.academic.service.services.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.onlineTests.Metadata;
import org.fenixedu.academic.domain.onlineTests.Question;
import org.fenixedu.academic.domain.onlineTests.utils.ParseSubQuestion;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.exceptions.tests.InvalidXMLFilesException;
import org.fenixedu.academic.utils.ParseQuestionException;

import pt.ist.fenixWebFramework.servlets.commons.UploadedFile;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class InsertExerciseVariation {

    private static final double FILE_SIZE_LIMIT = Math.pow(2, 20);

    protected List run(ExecutionCourse executionCourse, String metadataId, UploadedFile xmlZipFile) throws FenixServiceException,
            NotExecuteException {
        List<String> badXmls = new ArrayList<String>();

        Metadata metadata = FenixFramework.getDomainObject(metadataId);
        if (metadata == null) {
            throw new InvalidArgumentsServiceException();
        }
        List<LabelValueBean> xmlFilesList = getXmlFilesList(xmlZipFile);
        if (xmlFilesList == null || xmlFilesList.size() == 0) {
            throw new InvalidXMLFilesException();
        }

        for (LabelValueBean labelValueBean : xmlFilesList) {
            String xmlFile = labelValueBean.getValue();
            String xmlFileName = labelValueBean.getLabel();

            try {
                ParseSubQuestion parseQuestion = new ParseSubQuestion();

                parseQuestion.parseSubQuestion(xmlFile);
                Question question = new Question(metadata.correctFileName(xmlFileName), xmlFile, true);
                question.setMetadata(metadata);
            } catch (ParseQuestionException e) {
                badXmls.add(xmlFileName + e);
            }
        }

        return badXmls;
    }

    private List<LabelValueBean> getXmlFilesList(UploadedFile xmlZipFile) {
        List<LabelValueBean> xmlFilesList = new ArrayList<LabelValueBean>();
        ZipInputStream zipFile = null;

        try {
            if (xmlZipFile.getContentType().equals("text/xml") || xmlZipFile.getContentType().equals("application/xml")) {
                if (xmlZipFile.getSize() <= FILE_SIZE_LIMIT) {
                    xmlFilesList
                            .add(new LabelValueBean(xmlZipFile.getName(), new String(xmlZipFile.getFileData(), "ISO-8859-1")));
                }
            } else {
                zipFile = new ZipInputStream(xmlZipFile.getInputStream());
                for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry = zipFile.getNextEntry()) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    final byte[] b = new byte[1000];
                    for (int readed = 0; (readed = zipFile.read(b)) > -1; stringBuilder.append(new String(b, 0, readed,
                            "ISO-8859-1"))) {
                        // nothing to do :o)
                    }
                    if (stringBuilder.length() <= FILE_SIZE_LIMIT) {
                        xmlFilesList.add(new LabelValueBean(entry.getName(), stringBuilder.toString()));
                    }
                }
                zipFile.close();
            }
        } catch (Exception e) {
            return null;
        }
        return xmlFilesList;
    }

    // Service Invokers migrated from Berserk

    private static final InsertExerciseVariation serviceInstance = new InsertExerciseVariation();

    @Atomic
    public static List runInsertExerciseVariation(ExecutionCourse executionCourse, String metadataId, UploadedFile xmlZipFile)
            throws FenixServiceException, NotExecuteException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourse);
        return serviceInstance.run(executionCourse, metadataId, xmlZipFile);
    }

}