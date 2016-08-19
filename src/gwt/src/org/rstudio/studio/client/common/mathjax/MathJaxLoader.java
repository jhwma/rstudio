/*
 * MathJaxLoader.java
 *
 * Copyright (C) 2009-16 by RStudio, Inc.
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
package org.rstudio.studio.client.common.mathjax;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.ScriptElement;
import com.google.inject.Singleton;

@Singleton
public class MathJaxLoader
{
   public interface Callback
   {
      public void onLoaded(boolean alreadyLoaded);
   }
   
   public MathJaxLoader()
   {
      ensureMathJaxLoaded();
   }
   
   public static void ensureMathJaxLoaded()
   {
      ensureMathJaxLoaded(null);
   }
   
   public static void ensureMathJaxLoaded(Callback callback)
   {
      if (MATHJAX_LOADED)
      {
         callback.onLoaded(MATHJAX_LOADED);
         return;
      }
      
      initializeMathJaxConfig();
      ScriptElement mathJaxEl = createMathJaxScriptElement();
      HeadElement headEl = Document.get().getHead();
      headEl.appendChild(mathJaxEl);
      Scheduler.get().scheduleFixedDelay(new RepeatingCommand()
      {
         @Override
         public boolean execute()
         {
            if (isMathJaxReady())
            {
               MATHJAX_LOADED = true;
               onMathJaxLoaded();
               return false;
            }
            
            RETRY_COUNT++;
            return RETRY_COUNT < 20;
         }
      }, 50);
   }
   
   private static final native void onMathJaxLoaded() /*-{
      var MathJax = $wnd.MathJax;
      
      // avoid jittering
      MathJax.Hub.processSectionDelay = 0;
   }-*/;
   
   private static final native void initializeMathJaxConfig() /*-{

      if (typeof $wnd.MathJax !== "undefined")
         return;

      $wnd.MathJax = {
         extensions: ['tex2jax.js'],
         jax: ['input/TeX', 'output/HTML-CSS'],
         tex2jax: {
            inlineMath:  [['$', '$'], ['\\(', '\\)']],
            displayMath: [['$$', '$$'], ['\\[', '\\]']],
            processEscapes: true
         },
         "HTML-CSS": {
            availableFonts: ['TeX']
         },
         showProcessingMessage: false,
         showMathMenu: false,
         messageStyle: "none",
         skipStartupTypeset: true,
         menuSettings: {
            zoom: "None",
            context: "Browser"
         }
      };

   }-*/;
   
   private static final native boolean isMathJaxReady() /*-{
      var mathjax = $wnd.MathJax || {};
      return mathjax.isReady;
   }-*/;
   
   private static final native void setMathJaxHooks() /*-{
   }-*/;

   private static ScriptElement createMathJaxScriptElement()
   {
      ScriptElement el = Document.get().createScriptElement();
      el.setAttribute("type", "text/javascript");
      el.setSrc("mathjax/MathJax.js?config=TeX-MML-AM_CHTML");
      el.setAttribute("async", "true");
      return el;
   }
   
   private static boolean MATHJAX_LOADED = false;
   private static int RETRY_COUNT = 0;
   
}
