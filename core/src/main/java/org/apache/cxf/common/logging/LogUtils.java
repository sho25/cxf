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
name|common
operator|.
name|logging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|Constructor
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
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|MissingResourceException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|LogRecord
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
name|i18n
operator|.
name|BundleUtils
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
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * A container for static utility methods related to logging.  * By default, CXF logs to java.util.logging. An application can change this. To log to another system, the  * application must provide an object that extends {@link AbstractDelegatingLogger}, and advertise that class  * via one of the following mechanisms:  *<ul>  *<li>Create a file, in the classpath, named META-INF/cxf/org.apache.cxf.Logger.  * This file should contain the fully-qualified name  * of the class, with no comments, on a single line.</li>  *<li>Call {@link #setLoggerClass(Class)} with a Class<?> reference to the logger class.</li>  *</ul>  * CXF provides {@link Log4jLogger} to use log4j instead of java.util.logging.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|LogUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"org.apache.cxf.Logger"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Object
index|[]
name|NO_PARAMETERS
init|=
operator|new
name|Object
index|[
literal|0
index|]
decl_stmt|;
specifier|private
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|loggerClass
decl_stmt|;
comment|/**      * Prevents instantiation.      */
specifier|private
name|LogUtils
parameter_list|()
block|{     }
static|static
block|{
name|JDKBugHacks
operator|.
name|doHacks
argument_list|()
expr_stmt|;
try|try
block|{
name|String
name|cname
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cname
operator|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|run
parameter_list|()
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|KEY
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore - likely security exception or similar that won't allow
comment|//access to the system properties.   We'll continue with other methods
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|cname
argument_list|)
condition|)
block|{
name|InputStream
name|ins
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"META-INF/cxf/"
operator|+
name|KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|ins
operator|==
literal|null
condition|)
block|{
name|ins
operator|=
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
literal|"META-INF/cxf/"
operator|+
name|KEY
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ins
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|BufferedReader
name|din
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|ins
argument_list|)
argument_list|)
init|)
block|{
name|cname
operator|=
name|din
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|cname
argument_list|)
condition|)
block|{
try|try
block|{
comment|// This Class.forName likely will barf in OSGi, but it's OK
comment|// as we'll just use j.u.l and pax-logging will pick it up fine
comment|// If we don't call this and there isn't a slf4j impl avail,
comment|// you get warnings printed to stderr about NOPLoggers and such
name|Class
operator|.
name|forName
argument_list|(
literal|"org.slf4j.impl.StaticLoggerBinder"
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.slf4j.LoggerFactory"
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|fcls
init|=
name|cls
operator|.
name|getMethod
argument_list|(
literal|"getILoggerFactory"
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|String
name|clsName
init|=
name|fcls
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|clsName
operator|.
name|contains
argument_list|(
literal|"NOPLogger"
argument_list|)
condition|)
block|{
comment|//no real slf4j implementation, use j.u.l
name|cname
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clsName
operator|.
name|contains
argument_list|(
literal|"Log4j"
argument_list|)
condition|)
block|{
name|cname
operator|=
literal|"org.apache.cxf.common.logging.Log4jLogger"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clsName
operator|.
name|contains
argument_list|(
literal|"JCL"
argument_list|)
condition|)
block|{
name|cls
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.commons.logging.LogFactory"
argument_list|)
expr_stmt|;
name|fcls
operator|=
name|cls
operator|.
name|getMethod
argument_list|(
literal|"getFactory"
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
operator|.
name|getClass
argument_list|()
expr_stmt|;
if|if
condition|(
name|fcls
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Log4j"
argument_list|)
condition|)
block|{
name|cname
operator|=
literal|"org.apache.cxf.common.logging.Log4jLogger"
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|clsName
operator|.
name|contains
argument_list|(
literal|"JDK14"
argument_list|)
operator|||
name|clsName
operator|.
name|contains
argument_list|(
literal|"pax.logging"
argument_list|)
condition|)
block|{
comment|//both of these we can use the appropriate j.u.l API's
comment|//directly and have it work properly
name|cname
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
comment|// Cannot really detect where it's logging so we'll
comment|// go ahead and use the Slf4jLogger directly
name|cname
operator|=
literal|"org.apache.cxf.common.logging.Slf4jLogger"
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore - Slf4j not available
block|}
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|cname
argument_list|)
condition|)
block|{
try|try
block|{
name|loggerClass
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|cname
operator|.
name|trim
argument_list|()
argument_list|,
literal|true
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|loggerClass
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|cname
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getLogger
argument_list|(
name|LogUtils
operator|.
name|class
argument_list|)
operator|.
name|fine
argument_list|(
literal|"Using "
operator|+
name|loggerClass
operator|.
name|getName
argument_list|()
operator|+
literal|" for logging."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|//ignore - if we get here, some issue prevented the logger class from being loaded.
comment|//maybe a ClassNotFound or NoClassDefFound or similar.   Just use j.u.l
name|loggerClass
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Specify a logger class that inherits from {@link AbstractDelegatingLogger}.      * Enable users to use their own logger implementation.      */
specifier|public
specifier|static
name|void
name|setLoggerClass
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractDelegatingLogger
argument_list|>
name|cls
parameter_list|)
block|{
name|loggerClass
operator|=
name|cls
expr_stmt|;
block|}
comment|/**      * Get a Logger with the associated default resource bundle for the class.      *      * @param cls the Class to contain the Logger      * @return an appropriate Logger      */
specifier|public
specifier|static
name|Logger
name|getLogger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|createLogger
argument_list|(
name|cls
argument_list|,
literal|null
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Get a Logger with an associated resource bundle.      *      * @param cls the Class to contain the Logger      * @param resourcename the resource name      * @return an appropriate Logger      */
specifier|public
specifier|static
name|Logger
name|getLogger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|resourcename
parameter_list|)
block|{
return|return
name|createLogger
argument_list|(
name|cls
argument_list|,
name|resourcename
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Get a Logger with an associated resource bundle.      *      * @param cls the Class to contain the Logger (to find resources)      * @param resourcename the resource name      * @param loggerName the full name for the logger      * @return an appropriate Logger      */
specifier|public
specifier|static
name|Logger
name|getLogger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|resourcename
parameter_list|,
name|String
name|loggerName
parameter_list|)
block|{
return|return
name|createLogger
argument_list|(
name|cls
argument_list|,
name|resourcename
argument_list|,
name|loggerName
argument_list|)
return|;
block|}
comment|/**      * Get a Logger with the associated default resource bundle for the class.      *      * @param cls the Class to contain the Logger      * @return an appropriate Logger      */
specifier|public
specifier|static
name|Logger
name|getL7dLogger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|createLogger
argument_list|(
name|cls
argument_list|,
literal|null
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Get a Logger with an associated resource bundle.      *      * @param cls the Class to contain the Logger      * @param resourcename the resource name      * @return an appropriate Logger      */
specifier|public
specifier|static
name|Logger
name|getL7dLogger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|resourcename
parameter_list|)
block|{
return|return
name|createLogger
argument_list|(
name|cls
argument_list|,
name|resourcename
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Get a Logger with an associated resource bundle.      *      * @param cls the Class to contain the Logger (to find resources)      * @param resourcename the resource name      * @param loggerName the full name for the logger      * @return an appropriate Logger      */
specifier|public
specifier|static
name|Logger
name|getL7dLogger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|resourcename
parameter_list|,
name|String
name|loggerName
parameter_list|)
block|{
return|return
name|createLogger
argument_list|(
name|cls
argument_list|,
name|resourcename
argument_list|,
name|loggerName
argument_list|)
return|;
block|}
comment|/**      * Create a logger      */
specifier|protected
specifier|static
name|Logger
name|createLogger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|loggerName
parameter_list|)
block|{
name|ClassLoader
name|orig
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|ClassLoader
name|n
init|=
name|cls
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
name|setContextClassLoader
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
name|String
name|bundleName
init|=
name|name
decl_stmt|;
try|try
block|{
name|Logger
name|logger
init|=
literal|null
decl_stmt|;
name|ResourceBundle
name|b
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bundleName
operator|==
literal|null
condition|)
block|{
comment|//grab the bundle prior to the call to Logger.getLogger(...) so the
comment|//ResourceBundle can be loaded outside the big sync block that getLogger really is
name|bundleName
operator|=
name|BundleUtils
operator|.
name|getBundleName
argument_list|(
name|cls
argument_list|)
expr_stmt|;
try|try
block|{
name|b
operator|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MissingResourceException
name|rex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
else|else
block|{
name|bundleName
operator|=
name|BundleUtils
operator|.
name|getBundleName
argument_list|(
name|cls
argument_list|,
name|bundleName
argument_list|)
expr_stmt|;
try|try
block|{
name|b
operator|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|cls
argument_list|,
name|bundleName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MissingResourceException
name|rex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|getLocale
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|loggerClass
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|cns
init|=
name|loggerClass
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|(
name|Logger
operator|)
name|cns
operator|.
name|newInstance
argument_list|(
name|loggerName
argument_list|,
name|bundleName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ite
parameter_list|)
block|{
if|if
condition|(
name|ite
operator|.
name|getTargetException
argument_list|()
operator|instanceof
name|MissingResourceException
condition|)
block|{
return|return
operator|(
name|Logger
operator|)
name|cns
operator|.
name|newInstance
argument_list|(
name|loggerName
argument_list|,
literal|null
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
name|ite
throw|;
block|}
block|}
block|}
else|else
block|{
try|try
block|{
return|return
operator|(
name|Logger
operator|)
name|cns
operator|.
name|newInstance
argument_list|(
name|loggerName
argument_list|,
name|bundleName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ite
parameter_list|)
block|{
if|if
condition|(
name|ite
operator|.
name|getTargetException
argument_list|()
operator|instanceof
name|MissingResourceException
condition|)
block|{
throw|throw
operator|(
name|MissingResourceException
operator|)
name|ite
operator|.
name|getTargetException
argument_list|()
throw|;
block|}
else|else
block|{
throw|throw
name|ite
throw|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
try|try
block|{
name|logger
operator|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|loggerName
argument_list|,
name|bundleName
argument_list|)
expr_stmt|;
comment|//NOPMD
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|iae
parameter_list|)
block|{
comment|//likely a mismatch on the bundle name, just return the default
name|logger
operator|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|loggerName
argument_list|)
expr_stmt|;
comment|//NOPMD
block|}
catch|catch
parameter_list|(
name|MissingResourceException
name|rex
parameter_list|)
block|{
name|logger
operator|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|loggerName
argument_list|)
expr_stmt|;
comment|//NOPMD
block|}
finally|finally
block|{
name|b
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|logger
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|n
operator|!=
name|orig
condition|)
block|{
name|setContextClassLoader
argument_list|(
name|orig
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|setContextClassLoader
parameter_list|(
specifier|final
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|run
parameter_list|()
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Allows both parameter substitution and a typed Throwable to be logged.      *      * @param logger the Logger the log to      * @param level the severity level      * @param message the log message      * @param throwable the Throwable to log      * @param parameter the parameter to substitute into message      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|Level
name|level
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|,
name|Object
name|parameter
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|level
argument_list|)
condition|)
block|{
specifier|final
name|String
name|formattedMessage
init|=
name|MessageFormat
operator|.
name|format
argument_list|(
name|localize
argument_list|(
name|logger
argument_list|,
name|message
argument_list|)
argument_list|,
name|parameter
argument_list|)
decl_stmt|;
name|doLog
argument_list|(
name|logger
argument_list|,
name|level
argument_list|,
name|formattedMessage
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Allows both parameter substitution and a typed Throwable to be logged.      *      * @param logger the Logger the log to      * @param level the severity level      * @param message the log message      * @param throwable the Throwable to log      * @param parameters the parameters to substitute into message      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|Level
name|level
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|,
name|Object
modifier|...
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|level
argument_list|)
condition|)
block|{
specifier|final
name|String
name|formattedMessage
init|=
name|MessageFormat
operator|.
name|format
argument_list|(
name|localize
argument_list|(
name|logger
argument_list|,
name|message
argument_list|)
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|doLog
argument_list|(
name|logger
argument_list|,
name|level
argument_list|,
name|formattedMessage
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Checks log level and logs      *      * @param logger the Logger the log to      * @param level the severity level      * @param message the log message      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|Level
name|level
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|log
argument_list|(
name|logger
argument_list|,
name|level
argument_list|,
name|message
argument_list|,
name|NO_PARAMETERS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks log level and logs      *      * @param logger the Logger the log to      * @param level the severity level      * @param message the log message      * @param throwable the Throwable to log      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|Level
name|level
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
name|log
argument_list|(
name|logger
argument_list|,
name|level
argument_list|,
name|message
argument_list|,
name|throwable
argument_list|,
name|NO_PARAMETERS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks log level and logs      *      * @param logger the Logger the log to      * @param level the severity level      * @param message the log message      * @param parameter the parameter to substitute into message      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|Level
name|level
parameter_list|,
name|String
name|message
parameter_list|,
name|Object
name|parameter
parameter_list|)
block|{
name|log
argument_list|(
name|logger
argument_list|,
name|level
argument_list|,
name|message
argument_list|,
operator|new
name|Object
index|[]
block|{
name|parameter
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks log level and logs      *      * @param logger the Logger the log to      * @param level the severity level      * @param message the log message      * @param parameters the parameters to substitute into message      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|Level
name|level
parameter_list|,
name|String
name|message
parameter_list|,
name|Object
index|[]
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|level
argument_list|)
condition|)
block|{
name|String
name|msg
init|=
name|localize
argument_list|(
name|logger
argument_list|,
name|message
argument_list|)
decl_stmt|;
try|try
block|{
name|msg
operator|=
name|MessageFormat
operator|.
name|format
argument_list|(
name|msg
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|//ignore, log as is
block|}
name|doLog
argument_list|(
name|logger
argument_list|,
name|level
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|doLog
parameter_list|(
name|Logger
name|log
parameter_list|,
name|Level
name|level
parameter_list|,
name|String
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|LogRecord
name|record
init|=
operator|new
name|LogRecord
argument_list|(
name|level
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|record
operator|.
name|setLoggerName
argument_list|(
name|log
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setResourceBundleName
argument_list|(
name|log
operator|.
name|getResourceBundleName
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setResourceBundle
argument_list|(
name|log
operator|.
name|getResourceBundle
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|record
operator|.
name|setThrown
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
comment|//try to get the right class name/method name - just trace
comment|//back the stack till we get out of this class
name|StackTraceElement
name|stack
index|[]
init|=
operator|(
operator|new
name|Throwable
argument_list|()
operator|)
operator|.
name|getStackTrace
argument_list|()
decl_stmt|;
name|String
name|cname
init|=
name|LogUtils
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|stack
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|StackTraceElement
name|frame
init|=
name|stack
index|[
name|x
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|frame
operator|.
name|getClassName
argument_list|()
operator|.
name|equals
argument_list|(
name|cname
argument_list|)
condition|)
block|{
name|record
operator|.
name|setSourceClassName
argument_list|(
name|frame
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setSourceMethodName
argument_list|(
name|frame
operator|.
name|getMethodName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|log
operator|.
name|log
argument_list|(
name|record
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retrieve localized message retrieved from a logger's resource      * bundle.      *      * @param logger the Logger      * @param message the message to be localized      */
specifier|private
specifier|static
name|String
name|localize
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|ResourceBundle
name|bundle
init|=
name|logger
operator|.
name|getResourceBundle
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|bundle
operator|!=
literal|null
condition|?
name|bundle
operator|.
name|getString
argument_list|(
name|message
argument_list|)
else|:
name|message
return|;
block|}
catch|catch
parameter_list|(
name|MissingResourceException
name|ex
parameter_list|)
block|{
comment|//string not in the bundle
return|return
name|message
return|;
block|}
block|}
block|}
end_class

end_unit

