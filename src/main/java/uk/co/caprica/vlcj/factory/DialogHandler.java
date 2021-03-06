/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.factory;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.enums.DialogQuestionType;

public interface DialogHandler {

    void displayError(Pointer userData, String title, String text);

    void displayLogin(Pointer userData, DialogId id, String title, String text, String defaultUsername, boolean askStore);

    void displayQuestion(Pointer userData, DialogId id, String title, String text, DialogQuestionType type, String cancel, String action1, String action2);

    void displayProgress(Pointer userData, DialogId id, String title, String text, int indeterminate, float position, String cancel);

    void cancel(Pointer userData, DialogId id);

    void updateProgress(Pointer userData, DialogId id, float position, String text);

}
