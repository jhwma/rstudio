/*
 * UserStateComputedLayer.hpp
 *
 * Copyright (C) 2009-19 by RStudio, Inc.
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */

#ifndef SESSION_USER_STATE_COMPUTED_LAYER_HPP
#define SESSION_USER_STATE_COMPUTED_LAYER_HPP

#include <session/prefs/UserStateValues.hpp>

namespace rstudio {
namespace session {
namespace prefs {

class UserStateComputedLayer: public PrefLayer
{
public:
   UserStateComputedLayer();
   core::Error readPrefs();
   core::Error validatePrefs(const core::json::Object&);
};

} // namespace prefs
} // namespace session
} // namespace rstudio

#endif
