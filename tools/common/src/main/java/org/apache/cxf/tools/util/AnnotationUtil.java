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
name|tools
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
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
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebResult
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
name|URIParserUtil
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
name|tools
operator|.
name|common
operator|.
name|ToolException
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|AnnotationUtil
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
name|AnnotationUtil
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AnnotationUtil
parameter_list|()
block|{      }
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Annotation
parameter_list|>
name|T
name|getPrivClassAnnotation
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|anoClass
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
specifier|public
name|T
name|run
parameter_list|()
block|{
return|return
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|anoClass
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Annotation
parameter_list|>
name|T
name|getPrivMethodAnnotation
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|anoClass
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
specifier|public
name|T
name|run
parameter_list|()
block|{
return|return
name|method
operator|.
name|getAnnotation
argument_list|(
name|anoClass
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Annotation
index|[]
index|[]
name|getPrivParameterAnnotations
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|Annotation
index|[]
index|[]
argument_list|>
argument_list|()
block|{
specifier|public
name|Annotation
index|[]
index|[]
name|run
parameter_list|()
block|{
return|return
name|method
operator|.
name|getParameterAnnotations
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|URLClassLoader
name|getClassLoader
parameter_list|(
name|ClassLoader
name|parent
parameter_list|)
block|{
name|URL
index|[]
name|urls
init|=
name|URIParserUtil
operator|.
name|pathToURLs
argument_list|(
name|getClassPath
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|URLClassLoader
operator|)
name|ClassLoaderUtils
operator|.
name|getURLClassLoader
argument_list|(
name|urls
argument_list|,
name|parent
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|ClassLoader
name|newLoader
parameter_list|(
name|URL
index|[]
name|urls
parameter_list|,
name|ClassLoader
name|parent
parameter_list|)
block|{
return|return
name|ClassLoaderUtils
operator|.
name|getURLClassLoader
argument_list|(
name|urls
argument_list|,
name|parent
argument_list|)
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|className
parameter_list|,
name|ClassLoader
name|parent
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
literal|null
decl_stmt|;
name|URL
index|[]
name|urls
init|=
name|URIParserUtil
operator|.
name|pathToURLs
argument_list|(
name|getClassPath
argument_list|()
argument_list|)
decl_stmt|;
name|ClassLoader
name|classLoader
init|=
name|newLoader
argument_list|(
name|urls
argument_list|,
name|parent
argument_list|)
decl_stmt|;
try|try
block|{
name|clazz
operator|=
name|classLoader
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_LOAD_CLASS"
argument_list|,
name|LOG
argument_list|,
name|className
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|clazz
return|;
block|}
specifier|private
specifier|static
name|String
name|getClassPath
parameter_list|()
block|{
name|ClassLoader
name|loader
init|=
name|AnnotationUtil
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|StringBuilder
name|classpath
init|=
operator|new
name|StringBuilder
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.path"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|instanceof
name|URLClassLoader
condition|)
block|{
for|for
control|(
name|URL
name|url
range|:
operator|(
operator|(
name|URLClassLoader
operator|)
name|loader
operator|)
operator|.
name|getURLs
argument_list|()
control|)
block|{
name|classpath
operator|.
name|append
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|)
expr_stmt|;
name|classpath
operator|.
name|append
argument_list|(
name|url
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|classpath
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|WebParam
name|getWebParam
parameter_list|(
name|Method
name|method
parameter_list|,
name|String
name|paraName
parameter_list|)
block|{
name|Annotation
index|[]
index|[]
name|anno
init|=
name|getPrivParameterAnnotations
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|int
name|count
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|Annotation
name|ann
range|:
name|anno
index|[
name|i
index|]
control|)
block|{
if|if
condition|(
name|ann
operator|.
name|annotationType
argument_list|()
operator|==
name|WebParam
operator|.
name|class
condition|)
block|{
name|WebParam
name|webParam
init|=
operator|(
name|WebParam
operator|)
name|ann
decl_stmt|;
if|if
condition|(
name|paraName
operator|.
name|equals
argument_list|(
name|webParam
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|webParam
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|WebResult
name|getWebResult
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|Annotation
name|ann
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|WebResult
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|WebResult
operator|)
name|ann
return|;
block|}
block|}
end_class

end_unit

