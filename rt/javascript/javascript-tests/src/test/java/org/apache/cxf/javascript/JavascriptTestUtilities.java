begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|javascript
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|javascript
operator|.
name|service
operator|.
name|ServiceJavascriptBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|javascript
operator|.
name|types
operator|.
name|SchemaJavascriptBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|SchemaInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|test
operator|.
name|TestUtilities
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|ContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|JavaScriptException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|RhinoException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Scriptable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|ScriptableObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  * Test utilities class with some Javascript capability included.  */
end_comment

begin_class
specifier|public
class|class
name|JavascriptTestUtilities
extends|extends
name|TestUtilities
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JavascriptTestUtilities
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|rhinoDebuggerUp
decl_stmt|;
specifier|private
name|ContextFactory
name|rhinoContextFactory
decl_stmt|;
specifier|private
name|ScriptableObject
name|rhinoScope
decl_stmt|;
specifier|private
name|Context
name|rhinoContext
decl_stmt|;
specifier|public
specifier|static
class|class
name|JavaScriptAssertionFailed
extends|extends
name|RuntimeException
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4776862940267084055L
decl_stmt|;
specifier|public
name|JavaScriptAssertionFailed
parameter_list|(
name|String
name|what
parameter_list|)
block|{
name|super
argument_list|(
name|what
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|JsAssert
extends|extends
name|ScriptableObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|8541723718764399405L
decl_stmt|;
specifier|public
name|JsAssert
parameter_list|()
block|{         }
specifier|public
name|void
name|jsConstructor
parameter_list|(
name|String
name|exp
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Assertion failed: "
operator|+
name|exp
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JavaScriptAssertionFailed
argument_list|(
name|exp
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
literal|"Assert"
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|Trace
extends|extends
name|ScriptableObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8478529983202430575L
decl_stmt|;
specifier|public
name|Trace
parameter_list|()
block|{         }
comment|//when acting as a function
specifier|public
name|Trace
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|jsStaticFunction_trace
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
literal|"org_apache_cxf_trace"
return|;
block|}
comment|// CHECKSTYLE:OFF
specifier|public
specifier|static
name|void
name|jsStaticFunction_trace
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|// CHECKSTYLE:ON
block|}
specifier|public
specifier|static
class|class
name|Notifier
extends|extends
name|ScriptableObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4581431707286545806L
decl_stmt|;
specifier|private
name|boolean
name|notified
decl_stmt|;
specifier|public
name|Notifier
parameter_list|()
block|{         }
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
literal|"org_apache_cxf_notifier"
return|;
block|}
specifier|public
specifier|synchronized
name|boolean
name|waitForJavascript
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
while|while
condition|(
operator|!
name|notified
condition|)
block|{
try|try
block|{
name|wait
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
return|return
name|notified
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// do nothing.
block|}
block|}
return|return
literal|true
return|;
comment|// only here if true on entry.
block|}
comment|// CHECKSTYLE:OFF
specifier|public
specifier|synchronized
name|void
name|jsFunction_notify
parameter_list|()
block|{
name|notified
operator|=
literal|true
expr_stmt|;
name|notifyAll
argument_list|()
expr_stmt|;
block|}
comment|// CHECKSTYLE:ON
block|}
specifier|public
specifier|static
class|class
name|CountDownNotifier
extends|extends
name|ScriptableObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2057930200102896264L
decl_stmt|;
specifier|private
name|CountDownLatch
name|latch
decl_stmt|;
specifier|public
name|CountDownNotifier
parameter_list|()
block|{         }
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
literal|"org_apache_cxf_count_down_notifier"
return|;
block|}
specifier|public
specifier|synchronized
name|boolean
name|waitForJavascript
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
return|return
name|latch
operator|.
name|await
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
comment|// if it returns at all, we're done.
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
comment|// empty on purpose.
block|}
block|}
block|}
comment|// CHECKSTYLE:OFF
specifier|public
name|void
name|jsConstructor
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
name|count
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|jsFunction_count
parameter_list|()
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
comment|// CHECKSTYLE:ON
block|}
specifier|public
name|JavascriptTestUtilities
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classpathReference
parameter_list|)
block|{
name|super
argument_list|(
name|classpathReference
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initializeRhino
parameter_list|()
block|{
name|rhinoContextFactory
operator|=
operator|new
name|ContextFactory
argument_list|()
expr_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"cxf.jsdebug"
argument_list|)
operator|!=
literal|null
operator|&&
operator|!
name|rhinoDebuggerUp
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|debuggerMain
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"org.mozilla.javascript.tools.debugger.Main"
argument_list|,
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|debuggerMain
operator|!=
literal|null
condition|)
block|{
name|Method
name|mainMethod
init|=
name|debuggerMain
operator|.
name|getMethod
argument_list|(
literal|"mainEmbedded"
argument_list|,
name|ContextFactory
operator|.
name|class
argument_list|,
name|Scriptable
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|mainMethod
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|rhinoContextFactory
argument_list|,
name|rhinoScope
argument_list|,
literal|"Debug embedded JavaScript."
argument_list|)
expr_stmt|;
name|rhinoDebuggerUp
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Failed to launch Rhino debugger"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|rhinoContext
operator|=
name|rhinoContextFactory
operator|.
name|enterContext
argument_list|()
expr_stmt|;
name|rhinoScope
operator|=
name|rhinoContext
operator|.
name|initStandardObjects
argument_list|()
expr_stmt|;
try|try
block|{
name|ScriptableObject
operator|.
name|defineClass
argument_list|(
name|rhinoScope
argument_list|,
name|JsAssert
operator|.
name|class
argument_list|)
expr_stmt|;
name|ScriptableObject
operator|.
name|defineClass
argument_list|(
name|rhinoScope
argument_list|,
name|Trace
operator|.
name|class
argument_list|)
expr_stmt|;
name|ScriptableObject
operator|.
name|defineClass
argument_list|(
name|rhinoScope
argument_list|,
name|Notifier
operator|.
name|class
argument_list|)
expr_stmt|;
name|ScriptableObject
operator|.
name|defineClass
argument_list|(
name|rhinoScope
argument_list|,
name|CountDownNotifier
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// so that the stock test for IE can gracefully fail.
name|rhinoContext
operator|.
name|evaluateString
argument_list|(
name|rhinoScope
argument_list|,
literal|"var window = new Object();"
argument_list|,
literal|"<internal>"
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
decl||
name|InstantiationException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|Context
operator|.
name|exit
argument_list|()
expr_stmt|;
block|}
name|JsSimpleDomNode
operator|.
name|register
argument_list|(
name|rhinoScope
argument_list|)
expr_stmt|;
name|JsSimpleDomParser
operator|.
name|register
argument_list|(
name|rhinoScope
argument_list|)
expr_stmt|;
name|JsNamedNodeMap
operator|.
name|register
argument_list|(
name|rhinoScope
argument_list|)
expr_stmt|;
name|JsXMLHttpRequest
operator|.
name|register
argument_list|(
name|rhinoScope
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|readResourceIntoRhino
parameter_list|(
name|String
name|resourceClasspath
parameter_list|)
throws|throws
name|IOException
block|{
name|Reader
name|js
init|=
name|getResourceAsReader
argument_list|(
name|resourceClasspath
argument_list|)
decl_stmt|;
name|rhinoContextFactory
operator|.
name|enterContext
argument_list|(
name|rhinoContext
argument_list|)
expr_stmt|;
try|try
block|{
name|rhinoContext
operator|.
name|evaluateReader
argument_list|(
name|rhinoScope
argument_list|,
name|js
argument_list|,
name|resourceClasspath
argument_list|,
literal|1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Context
operator|.
name|exit
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|readStringIntoRhino
parameter_list|(
name|String
name|js
parameter_list|,
name|String
name|sourceName
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|sourceName
operator|+
literal|":\n"
operator|+
name|js
argument_list|)
expr_stmt|;
name|rhinoContextFactory
operator|.
name|enterContext
argument_list|(
name|rhinoContext
argument_list|)
expr_stmt|;
try|try
block|{
name|rhinoContext
operator|.
name|evaluateString
argument_list|(
name|rhinoScope
argument_list|,
name|js
argument_list|,
name|sourceName
argument_list|,
literal|1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Context
operator|.
name|exit
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|ScriptableObject
name|getRhinoScope
parameter_list|()
block|{
return|return
name|rhinoScope
return|;
block|}
specifier|public
name|ContextFactory
name|getRhinoContextFactory
parameter_list|()
block|{
return|return
name|rhinoContextFactory
return|;
block|}
specifier|public
interface|interface
name|JSRunnable
parameter_list|<
name|T
parameter_list|>
block|{
name|T
name|run
parameter_list|(
name|Context
name|context
parameter_list|)
function_decl|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|runInsideContext
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|JSRunnable
argument_list|<
name|?
argument_list|>
name|runnable
parameter_list|)
block|{
name|rhinoContextFactory
operator|.
name|enterContext
argument_list|(
name|rhinoContext
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|runnable
operator|.
name|run
argument_list|(
name|rhinoContext
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|Context
operator|.
name|exit
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|javaToJS
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|Context
operator|.
name|javaToJS
argument_list|(
name|value
argument_list|,
name|rhinoScope
argument_list|)
return|;
block|}
specifier|public
name|Object
name|rhinoNewObject
parameter_list|(
specifier|final
name|String
name|constructorName
parameter_list|)
block|{
return|return
name|runInsideContext
argument_list|(
name|Object
operator|.
name|class
argument_list|,
operator|new
name|JSRunnable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|run
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|newObject
argument_list|(
name|rhinoScope
argument_list|,
name|constructorName
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Evaluate a javascript expression, returning the raw Rhino object.      *      * @param jsExpression the javascript expression.      * @return return value.      */
specifier|public
name|Object
name|rhinoEvaluate
parameter_list|(
specifier|final
name|String
name|jsExpression
parameter_list|)
block|{
return|return
name|runInsideContext
argument_list|(
name|Object
operator|.
name|class
argument_list|,
operator|new
name|JSRunnable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|run
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
return|return
name|rhinoContext
operator|.
name|evaluateString
argument_list|(
name|rhinoScope
argument_list|,
name|jsExpression
argument_list|,
literal|"<testcase>"
argument_list|,
literal|1
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Call a method on a Javascript object.      *      * @param that the object.      * @param methodName method name.      * @param args arguments.      * @return      */
specifier|public
name|Object
name|rhinoCallMethod
parameter_list|(
name|Scriptable
name|that
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|ScriptableObject
operator|.
name|callMethod
argument_list|(
name|rhinoContext
argument_list|,
name|that
argument_list|,
name|methodName
argument_list|,
name|args
argument_list|)
return|;
block|}
comment|/**      * Call a method on a Javascript object and convert result to specified class. Convert to the requested      * class.      *      * @param<T> type      * @param clazz class object.      * @param that Javascript object.      * @param methodName method      * @param args arguments      * @return return value.      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|rhinoCallMethodConvert
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Scriptable
name|that
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|Context
operator|.
name|jsToJava
argument_list|(
name|rhinoCallMethod
argument_list|(
name|that
argument_list|,
name|methodName
argument_list|,
name|args
argument_list|)
argument_list|,
name|clazz
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Call a method on a Javascript object inside context brackets.      *      * @param<T> return type.      * @param clazz class for the return type.      * @param that object      * @param methodName method      * @param args arguments. Caller must run javaToJS as appropriate      * @return return value.      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|rhinoCallMethodInContext
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
specifier|final
name|Scriptable
name|that
parameter_list|,
specifier|final
name|String
name|methodName
parameter_list|,
specifier|final
name|Object
modifier|...
name|args
parameter_list|)
block|{
comment|// we end up performing the cast twice to make the compiler happy.
return|return
name|runInsideContext
argument_list|(
name|clazz
argument_list|,
operator|new
name|JSRunnable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
specifier|public
name|T
name|run
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
return|return
name|rhinoCallMethodConvert
argument_list|(
name|clazz
argument_list|,
name|that
argument_list|,
name|methodName
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Evaluate a Javascript expression, converting the return value to a convenient Java type.      *      * @param<T> The desired type      * @param jsExpression the javascript expression.      * @param clazz the Class object for the desired type.      * @return the result.      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|rhinoEvaluateConvert
parameter_list|(
name|String
name|jsExpression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|Context
operator|.
name|jsToJava
argument_list|(
name|rhinoEvaluate
argument_list|(
name|jsExpression
argument_list|)
argument_list|,
name|clazz
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Call a JavaScript function within the Context. Optionally, require it to throw an exception equal to a      * supplied object. If the exception is called for, this function will either return null or Assert.      *      * @param expectingException Exception desired, or null.      * @param functionName Function to call.      * @param args args for the function. Be sure to Javascript-ify them as appropriate.      * @return      */
specifier|public
name|Object
name|rhinoCallExpectingExceptionInContext
parameter_list|(
specifier|final
name|Object
name|expectingException
parameter_list|,
specifier|final
name|String
name|functionName
parameter_list|,
specifier|final
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|runInsideContext
argument_list|(
name|Object
operator|.
name|class
argument_list|,
operator|new
name|JSRunnable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|run
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
return|return
name|rhinoCallExpectingException
argument_list|(
name|expectingException
argument_list|,
name|functionName
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Call a Javascript function, identified by name, on a set of arguments. Optionally, expect it to throw      * an exception.      *      * @param expectingException      * @param functionName      * @param args      * @return      */
specifier|public
name|Object
name|rhinoCallExpectingException
parameter_list|(
specifier|final
name|Object
name|expectingException
parameter_list|,
specifier|final
name|String
name|functionName
parameter_list|,
specifier|final
name|Object
modifier|...
name|args
parameter_list|)
block|{
name|Object
name|fObj
init|=
name|rhinoScope
operator|.
name|get
argument_list|(
name|functionName
argument_list|,
name|rhinoScope
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|fObj
operator|instanceof
name|Function
operator|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Missing test function "
operator|+
name|functionName
argument_list|)
throw|;
block|}
name|Function
name|function
init|=
operator|(
name|Function
operator|)
name|fObj
decl_stmt|;
try|try
block|{
return|return
name|function
operator|.
name|call
argument_list|(
name|rhinoContext
argument_list|,
name|rhinoScope
argument_list|,
name|rhinoScope
argument_list|,
name|args
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RhinoException
name|angryRhino
parameter_list|)
block|{
if|if
condition|(
name|expectingException
operator|!=
literal|null
operator|&&
name|angryRhino
operator|instanceof
name|JavaScriptException
condition|)
block|{
name|JavaScriptException
name|jse
init|=
operator|(
name|JavaScriptException
operator|)
name|angryRhino
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|jse
operator|.
name|getValue
argument_list|()
argument_list|,
name|expectingException
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|String
name|trace
init|=
name|angryRhino
operator|.
name|getScriptStackTrace
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"JavaScript error: "
operator|+
name|angryRhino
operator|.
name|toString
argument_list|()
operator|+
literal|" "
operator|+
name|trace
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JavaScriptAssertionFailed
name|assertion
parameter_list|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
name|assertion
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Object
name|rhinoCallInContext
parameter_list|(
name|String
name|functionName
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|rhinoCallExpectingExceptionInContext
argument_list|(
literal|null
argument_list|,
name|functionName
argument_list|,
name|args
argument_list|)
return|;
block|}
specifier|public
name|Object
name|rhinoCall
parameter_list|(
name|String
name|functionName
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|rhinoCallExpectingException
argument_list|(
literal|null
argument_list|,
name|functionName
argument_list|,
name|args
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|rhinoCallConvert
parameter_list|(
name|String
name|functionName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|Context
operator|.
name|jsToJava
argument_list|(
name|rhinoCallInContext
argument_list|(
name|functionName
argument_list|,
name|args
argument_list|)
argument_list|,
name|clazz
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|loadJavascriptForService
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|)
block|{
name|Collection
argument_list|<
name|SchemaInfo
argument_list|>
name|schemata
init|=
name|serviceInfo
operator|.
name|getSchemas
argument_list|()
decl_stmt|;
name|BasicNameManager
name|nameManager
init|=
name|BasicNameManager
operator|.
name|newNameManager
argument_list|(
name|serviceInfo
argument_list|)
decl_stmt|;
name|NamespacePrefixAccumulator
name|prefixManager
init|=
operator|new
name|NamespacePrefixAccumulator
argument_list|(
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|SchemaInfo
name|schema
range|:
name|schemata
control|)
block|{
name|SchemaJavascriptBuilder
name|builder
init|=
operator|new
name|SchemaJavascriptBuilder
argument_list|(
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
argument_list|,
name|prefixManager
argument_list|,
name|nameManager
argument_list|)
decl_stmt|;
name|String
name|allThatJavascript
init|=
name|builder
operator|.
name|generateCodeForSchema
argument_list|(
name|schema
operator|.
name|getSchema
argument_list|()
argument_list|)
decl_stmt|;
name|readStringIntoRhino
argument_list|(
name|allThatJavascript
argument_list|,
name|schema
operator|.
name|toString
argument_list|()
operator|+
literal|".js"
argument_list|)
expr_stmt|;
block|}
name|ServiceJavascriptBuilder
name|serviceBuilder
init|=
operator|new
name|ServiceJavascriptBuilder
argument_list|(
name|serviceInfo
argument_list|,
literal|null
argument_list|,
name|prefixManager
argument_list|,
name|nameManager
argument_list|)
decl_stmt|;
name|serviceBuilder
operator|.
name|walk
argument_list|()
expr_stmt|;
name|String
name|serviceJavascript
init|=
name|serviceBuilder
operator|.
name|getCode
argument_list|()
decl_stmt|;
name|readStringIntoRhino
argument_list|(
name|serviceJavascript
argument_list|,
name|serviceInfo
operator|.
name|getName
argument_list|()
operator|+
literal|".js"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|scriptableToString
parameter_list|(
name|Scriptable
name|scriptable
parameter_list|)
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|propid
range|:
name|scriptable
operator|.
name|getIds
argument_list|()
control|)
block|{
name|String
name|propIdString
init|=
name|Context
operator|.
name|toString
argument_list|(
name|propid
argument_list|)
decl_stmt|;
name|int
name|propIntKey
init|=
operator|-
literal|1
decl_stmt|;
try|try
block|{
name|propIntKey
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|propIdString
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
comment|// dummy.
block|}
name|String
name|propValue
decl_stmt|;
if|if
condition|(
name|propIntKey
operator|>=
literal|0
condition|)
block|{
name|propValue
operator|=
name|Context
operator|.
name|toString
argument_list|(
name|scriptable
operator|.
name|get
argument_list|(
name|propIntKey
argument_list|,
name|scriptable
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|propValue
operator|=
name|Context
operator|.
name|toString
argument_list|(
name|scriptable
operator|.
name|get
argument_list|(
name|propIdString
argument_list|,
name|scriptable
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|propIdString
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|propValue
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

