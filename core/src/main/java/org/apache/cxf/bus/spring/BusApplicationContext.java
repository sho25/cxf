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
name|bus
operator|.
name|spring
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
name|IOException
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessControlException
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
name|security
operator|.
name|PrivilegedActionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|i18n
operator|.
name|Message
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
name|common
operator|.
name|util
operator|.
name|SystemPropertyAction
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
name|configuration
operator|.
name|Configurer
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
name|interceptor
operator|.
name|Fault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|DefaultListableBeanFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|BeansDtdResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|DefaultNamespaceHandlerResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|NamespaceHandlerResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|PluggableSchemaResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|ResourceEntityResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|XmlBeanDefinitionReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ClassPathResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|FileSystemResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|UrlResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|support
operator|.
name|PathMatchingResourcePatternResolver
import|;
end_import

begin_class
specifier|public
class|class
name|BusApplicationContext
extends|extends
name|ClassPathXmlApplicationContext
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CXF_CFG_FILE
init|=
literal|"META-INF/cxf/cxf.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CXF_EXT_CFG_FILE
init|=
literal|"classpath*:META-INF/cxf/cxf.extension"
decl_stmt|;
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
name|BusApplicationContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|NamespaceHandlerResolver
name|nsHandlerResolver
decl_stmt|;
specifier|private
name|boolean
name|includeDefaults
decl_stmt|;
specifier|private
name|String
index|[]
name|cfgFiles
decl_stmt|;
specifier|private
name|URL
index|[]
name|cfgFileURLs
decl_stmt|;
specifier|public
name|BusApplicationContext
parameter_list|(
name|String
name|cf
parameter_list|,
name|boolean
name|include
parameter_list|)
block|{
name|this
argument_list|(
name|cf
argument_list|,
name|include
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BusApplicationContext
parameter_list|(
name|String
index|[]
name|cfs
parameter_list|,
name|boolean
name|include
parameter_list|)
block|{
name|this
argument_list|(
name|cfs
argument_list|,
name|include
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BusApplicationContext
parameter_list|(
name|URL
name|url
parameter_list|,
name|boolean
name|include
parameter_list|)
block|{
name|this
argument_list|(
name|url
argument_list|,
name|include
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BusApplicationContext
parameter_list|(
name|URL
index|[]
name|urls
parameter_list|,
name|boolean
name|include
parameter_list|)
block|{
name|this
argument_list|(
name|urls
argument_list|,
name|include
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BusApplicationContext
parameter_list|(
name|String
name|cf
parameter_list|,
name|boolean
name|include
parameter_list|,
name|ApplicationContext
name|parent
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|String
index|[]
block|{
name|cf
block|}
argument_list|,
name|include
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BusApplicationContext
parameter_list|(
name|URL
name|url
parameter_list|,
name|boolean
name|include
parameter_list|,
name|ApplicationContext
name|parent
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|URL
index|[]
block|{
name|url
block|}
argument_list|,
name|include
argument_list|,
name|parent
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BusApplicationContext
parameter_list|(
name|String
index|[]
name|cf
parameter_list|,
name|boolean
name|include
parameter_list|,
name|ApplicationContext
name|parent
parameter_list|)
block|{
name|this
argument_list|(
name|cf
argument_list|,
name|include
argument_list|,
name|parent
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BusApplicationContext
parameter_list|(
name|String
index|[]
name|cf
parameter_list|,
name|boolean
name|include
parameter_list|,
name|ApplicationContext
name|parent
parameter_list|,
name|NamespaceHandlerResolver
name|res
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
literal|false
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|cfgFiles
operator|=
name|cf
expr_stmt|;
name|includeDefaults
operator|=
name|include
expr_stmt|;
name|nsHandlerResolver
operator|=
name|res
expr_stmt|;
try|try
block|{
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|refresh
argument_list|()
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getException
argument_list|()
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|e
operator|.
name|getException
argument_list|()
throw|;
block|}
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|BusApplicationContext
parameter_list|(
name|URL
index|[]
name|url
parameter_list|,
name|boolean
name|include
parameter_list|,
name|ApplicationContext
name|parent
parameter_list|)
block|{
name|this
argument_list|(
name|url
argument_list|,
name|include
argument_list|,
name|parent
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BusApplicationContext
parameter_list|(
name|URL
index|[]
name|url
parameter_list|,
name|boolean
name|include
parameter_list|,
name|ApplicationContext
name|parent
parameter_list|,
name|NamespaceHandlerResolver
name|res
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
literal|false
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|cfgFileURLs
operator|=
name|url
expr_stmt|;
name|includeDefaults
operator|=
name|include
expr_stmt|;
name|nsHandlerResolver
operator|=
name|res
expr_stmt|;
try|try
block|{
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|refresh
argument_list|()
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getException
argument_list|()
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|e
operator|.
name|getException
argument_list|()
throw|;
block|}
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Resource
index|[]
name|getConfigResources
parameter_list|()
block|{
name|List
argument_list|<
name|Resource
argument_list|>
name|resources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|includeDefaults
condition|)
block|{
try|try
block|{
name|PathMatchingResourcePatternResolver
name|resolver
init|=
operator|new
name|PathMatchingResourcePatternResolver
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|resources
argument_list|,
name|resolver
operator|.
name|getResources
argument_list|(
name|DEFAULT_CXF_CFG_FILE
argument_list|)
argument_list|)
expr_stmt|;
name|Resource
index|[]
name|exts
init|=
name|resolver
operator|.
name|getResources
argument_list|(
name|DEFAULT_CXF_EXT_CFG_FILE
argument_list|)
decl_stmt|;
for|for
control|(
name|Resource
name|r
range|:
name|exts
control|)
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|r
operator|.
name|getInputStream
argument_list|()
init|;                         BufferedReader rd = new BufferedReader(new InputStreamReader(is
operator|,
init|StandardCharsets.UTF_8)
block|)
block|)
block|{
name|String
name|line
init|=
name|rd
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|line
argument_list|)
condition|)
block|{
name|resources
operator|.
name|add
argument_list|(
name|resolver
operator|.
name|getResource
argument_list|(
name|line
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|line
operator|=
name|rd
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
end_class

begin_decl_stmt
name|boolean
name|usingDefault
init|=
literal|false
decl_stmt|;
end_decl_stmt

begin_if
if|if
condition|(
literal|null
operator|==
name|cfgFiles
condition|)
block|{
name|String
name|cfgFile
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|Configurer
operator|.
name|USER_CFG_FILE_PROPERTY_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfgFile
operator|!=
literal|null
condition|)
block|{
name|cfgFiles
operator|=
operator|new
name|String
index|[]
block|{
name|cfgFile
block|}
expr_stmt|;
block|}
block|}
end_if

begin_if
if|if
condition|(
literal|null
operator|==
name|cfgFiles
condition|)
block|{
name|cfgFiles
operator|=
operator|new
name|String
index|[]
block|{
name|Configurer
operator|.
name|DEFAULT_USER_CFG_FILE
block|}
expr_stmt|;
name|usingDefault
operator|=
literal|true
expr_stmt|;
block|}
end_if

begin_for
for|for
control|(
name|String
name|cfgFile
range|:
name|cfgFiles
control|)
block|{
specifier|final
name|Resource
name|cpr
init|=
name|findResource
argument_list|(
name|cfgFile
argument_list|)
decl_stmt|;
name|boolean
name|exists
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|run
parameter_list|()
block|{
return|return
name|cpr
operator|!=
literal|null
operator|&&
name|cpr
operator|.
name|exists
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|exists
condition|)
block|{
name|resources
operator|.
name|add
argument_list|(
name|cpr
argument_list|)
expr_stmt|;
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|INFO
argument_list|,
literal|"USER_CFG_FILE_IN_USE"
argument_list|,
name|cfgFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|usingDefault
condition|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"USER_CFG_FILE_NOT_LOADED"
argument_list|,
name|cfgFile
argument_list|)
expr_stmt|;
name|String
name|message
init|=
operator|(
operator|new
name|Message
argument_list|(
literal|"USER_CFG_FILE_NOT_LOADED"
argument_list|,
name|LOG
argument_list|,
name|cfgFile
argument_list|)
operator|)
operator|.
name|toString
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|ApplicationContextException
argument_list|(
name|message
argument_list|)
throw|;
block|}
block|}
block|}
end_for

begin_if
if|if
condition|(
literal|null
operator|!=
name|cfgFileURLs
condition|)
block|{
for|for
control|(
name|URL
name|cfgFileURL
range|:
name|cfgFileURLs
control|)
block|{
name|UrlResource
name|ur
init|=
operator|new
name|UrlResource
argument_list|(
name|cfgFileURL
argument_list|)
decl_stmt|;
if|if
condition|(
name|ur
operator|.
name|exists
argument_list|()
condition|)
block|{
name|resources
operator|.
name|add
argument_list|(
name|ur
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"USER_CFG_FILE_URL_NOT_FOUND_MSG"
argument_list|,
name|cfgFileURL
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_if

begin_decl_stmt
name|String
name|sysCfgFileUrl
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|Configurer
operator|.
name|USER_CFG_FILE_PROPERTY_URL
argument_list|)
decl_stmt|;
end_decl_stmt

begin_if
if|if
condition|(
literal|null
operator|!=
name|sysCfgFileUrl
condition|)
block|{
try|try
block|{
name|UrlResource
name|ur
init|=
operator|new
name|UrlResource
argument_list|(
name|sysCfgFileUrl
argument_list|)
decl_stmt|;
if|if
condition|(
name|ur
operator|.
name|exists
argument_list|()
condition|)
block|{
name|resources
operator|.
name|add
argument_list|(
name|ur
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"USER_CFG_FILE_URL_NOT_FOUND_MSG"
argument_list|,
name|sysCfgFileUrl
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"USER_CFG_FILE_URL_ERROR_MSG"
argument_list|,
name|sysCfgFileUrl
argument_list|)
expr_stmt|;
block|}
block|}
end_if

begin_if
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Creating application context with resources: "
operator|+
name|resources
argument_list|)
expr_stmt|;
block|}
end_if

begin_if
if|if
condition|(
literal|0
operator|==
name|resources
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
end_if

begin_decl_stmt
name|Resource
index|[]
name|res
init|=
operator|new
name|Resource
index|[
name|resources
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|res
operator|=
name|resources
operator|.
name|toArray
argument_list|(
name|res
argument_list|)
expr_stmt|;
end_expr_stmt

begin_return
return|return
name|res
return|;
end_return

begin_function
unit|}          public
specifier|static
name|Resource
name|findResource
parameter_list|(
specifier|final
name|String
name|cfgFile
parameter_list|)
block|{
try|try
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|Resource
argument_list|>
argument_list|()
block|{
specifier|public
name|Resource
name|run
parameter_list|()
block|{
name|Resource
name|cpr
init|=
operator|new
name|ClassPathResource
argument_list|(
name|cfgFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|cpr
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|cpr
return|;
block|}
try|try
block|{
comment|//see if it's a URL
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|cfgFile
argument_list|)
decl_stmt|;
name|cpr
operator|=
operator|new
name|UrlResource
argument_list|(
name|url
argument_list|)
expr_stmt|;
if|if
condition|(
name|cpr
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|cpr
return|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
comment|//try loading it our way
name|URL
name|url
init|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|cfgFile
argument_list|,
name|BusApplicationContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|cpr
operator|=
operator|new
name|UrlResource
argument_list|(
name|url
argument_list|)
expr_stmt|;
if|if
condition|(
name|cpr
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|cpr
return|;
block|}
block|}
name|cpr
operator|=
operator|new
name|FileSystemResource
argument_list|(
name|cfgFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|cpr
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|cpr
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|AccessControlException
name|ex
parameter_list|)
block|{
comment|//cannot read the user config file
return|return
literal|null
return|;
block|}
block|}
end_function

begin_function
annotation|@
name|Override
specifier|protected
name|void
name|initBeanDefinitionReader
parameter_list|(
name|XmlBeanDefinitionReader
name|reader
parameter_list|)
block|{
comment|// Spring always creates a new one of these, which takes a fair amount
comment|// of time on startup (nearly 1/2 second) as it gets created for every
comment|// spring context on the classpath
if|if
condition|(
name|nsHandlerResolver
operator|==
literal|null
condition|)
block|{
name|nsHandlerResolver
operator|=
operator|new
name|DefaultNamespaceHandlerResolver
argument_list|()
expr_stmt|;
block|}
name|reader
operator|.
name|setNamespaceHandlerResolver
argument_list|(
name|nsHandlerResolver
argument_list|)
expr_stmt|;
name|String
name|mode
init|=
name|getSpringValidationMode
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|mode
condition|)
block|{
name|reader
operator|.
name|setValidationModeName
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setEntityResolvers
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|static
name|String
name|getSpringValidationMode
parameter_list|()
block|{
return|return
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
name|String
name|mode
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"org.apache.cxf.spring.validation.mode"
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|==
literal|null
condition|)
block|{
name|mode
operator|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"spring.validation.mode"
argument_list|)
expr_stmt|;
block|}
return|return
name|mode
return|;
block|}
block|}
argument_list|)
return|;
block|}
end_function

begin_function
name|void
name|setEntityResolvers
parameter_list|(
name|XmlBeanDefinitionReader
name|reader
parameter_list|)
block|{
name|ClassLoader
name|cl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|reader
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|BusEntityResolver
argument_list|(
name|cl
argument_list|,
operator|new
name|BeansDtdResolver
argument_list|()
argument_list|,
operator|new
name|PluggableSchemaResolver
argument_list|(
name|cl
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|protected
name|void
name|loadBeanDefinitions
parameter_list|(
name|DefaultListableBeanFactory
name|beanFactory
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Create a new XmlBeanDefinitionReader for the given BeanFactory.
name|XmlBeanDefinitionReader
name|beanDefinitionReader
init|=
operator|new
name|ControlledValidationXmlBeanDefinitionReader
argument_list|(
name|beanFactory
argument_list|)
decl_stmt|;
name|beanDefinitionReader
operator|.
name|setNamespaceHandlerResolver
argument_list|(
name|nsHandlerResolver
argument_list|)
expr_stmt|;
comment|// Configure the bean definition reader with this context's
comment|// resource loading environment.
name|beanDefinitionReader
operator|.
name|setResourceLoader
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|beanDefinitionReader
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|ResourceEntityResolver
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
comment|// Allow a subclass to provide custom initialization of the reader,
comment|// then proceed with actually loading the bean definitions.
name|initBeanDefinitionReader
argument_list|(
name|beanDefinitionReader
argument_list|)
expr_stmt|;
name|loadBeanDefinitions
argument_list|(
name|beanDefinitionReader
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

