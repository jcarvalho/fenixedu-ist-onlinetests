/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util.tests;

import java.util.List;

import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.util.FenixUtil;

public class QuestionOption extends FenixUtil {

    private String optionId;

    private List<LabelValueBean> optionContent;

    private boolean emptyResponse = false;

    public QuestionOption() {
        super();
    }

    public QuestionOption(String optionId) {
        super();
        this.optionId = optionId;
    }

    public boolean getEmptyResponse() {
        return emptyResponse;
    }

    public void setEmptyResponse(boolean emptyResponse) {
        this.emptyResponse = emptyResponse;
    }

    public List<LabelValueBean> getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(List<LabelValueBean> optionContent) {
        this.optionContent = optionContent;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }
}