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
name|jaxrs
operator|.
name|model
operator|.
name|wadl
package|;
end_package

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
name|util
operator|.
name|LinkedList
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
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
name|Bus
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
name|BusFactory
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
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
name|jaxrs
operator|.
name|model
operator|.
name|ClassResourceInfo
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
name|jaxrs
operator|.
name|model
operator|.
name|OperationResourceInfo
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
import|;
end_import

begin_class
specifier|public
class|class
name|JavaDocProvider
implements|implements
name|DocumentationProvider
block|{
specifier|public
specifier|static
specifier|final
name|double
name|JAVA_VERSION
init|=
name|getVersion
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|double
name|JAVA_VERSION_16
init|=
literal|1.6D
decl_stmt|;
specifier|private
name|ClassLoader
name|javaDocLoader
decl_stmt|;
specifier|private
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|ClassDocs
argument_list|>
name|docs
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|ClassDocs
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|double
name|javaDocsBuiltByVersion
init|=
name|JAVA_VERSION
decl_stmt|;
specifier|public
name|JavaDocProvider
parameter_list|(
name|URL
name|javaDocUrl
parameter_list|)
block|{
if|if
condition|(
name|javaDocUrl
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"URL is null"
argument_list|)
throw|;
block|}
name|javaDocLoader
operator|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|javaDocUrl
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JavaDocProvider
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
name|this
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JavaDocProvider
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
name|this
argument_list|(
name|ResourceUtils
operator|.
name|getResourceURL
argument_list|(
name|path
argument_list|,
name|bus
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|double
name|getVersion
parameter_list|()
block|{
name|String
name|version
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|Double
operator|.
name|parseDouble
argument_list|(
name|version
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
name|JAVA_VERSION_16
return|;
block|}
block|}
specifier|public
name|String
name|getClassDoc
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
try|try
block|{
name|ClassDocs
name|doc
init|=
name|getClassDocInternal
argument_list|(
name|cri
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|doc
operator|.
name|getClassInfo
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getMethodDoc
parameter_list|(
name|OperationResourceInfo
name|ori
parameter_list|)
block|{
try|try
block|{
name|MethodDocs
name|doc
init|=
name|getOperationDocInternal
argument_list|(
name|ori
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|doc
operator|.
name|getMethodInfo
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getMethodResponseDoc
parameter_list|(
name|OperationResourceInfo
name|ori
parameter_list|)
block|{
try|try
block|{
name|MethodDocs
name|doc
init|=
name|getOperationDocInternal
argument_list|(
name|ori
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|doc
operator|.
name|getResponseInfo
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getMethodParameterDoc
parameter_list|(
name|OperationResourceInfo
name|ori
parameter_list|,
name|int
name|paramIndex
parameter_list|)
block|{
try|try
block|{
name|MethodDocs
name|doc
init|=
name|getOperationDocInternal
argument_list|(
name|ori
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
name|doc
operator|.
name|getParamInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|paramIndex
operator|<
name|params
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|params
operator|.
name|get
argument_list|(
name|paramIndex
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|getPathAnnotatedClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Path
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|cls
return|;
block|}
if|if
condition|(
name|cls
operator|.
name|getSuperclass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Path
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|cls
operator|.
name|getSuperclass
argument_list|()
return|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|i
range|:
name|cls
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
if|if
condition|(
name|i
operator|.
name|getAnnotation
argument_list|(
name|Path
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
name|cls
return|;
block|}
specifier|private
name|ClassDocs
name|getClassDocInternal
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|annotatedClass
init|=
name|getPathAnnotatedClass
argument_list|(
name|cri
operator|.
name|getServiceClass
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|resource
init|=
name|annotatedClass
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|".html"
decl_stmt|;
name|ClassDocs
name|classDocs
init|=
name|docs
operator|.
name|get
argument_list|(
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
name|classDocs
operator|==
literal|null
condition|)
block|{
name|InputStream
name|resourceStream
init|=
name|javaDocLoader
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceStream
operator|!=
literal|null
condition|)
block|{
name|String
name|doc
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|resourceStream
argument_list|)
decl_stmt|;
name|String
name|qualifier
init|=
name|annotatedClass
operator|.
name|isInterface
argument_list|()
condition|?
literal|"Interface"
else|:
literal|"Class"
decl_stmt|;
name|String
name|classMarker
init|=
name|qualifier
operator|+
literal|" "
operator|+
name|annotatedClass
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|doc
operator|.
name|indexOf
argument_list|(
name|classMarker
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|classInfoTag
init|=
name|getClassInfoTag
argument_list|()
decl_stmt|;
name|String
name|classInfo
init|=
name|getJavaDocText
argument_list|(
name|doc
argument_list|,
name|classInfoTag
argument_list|,
literal|"Method Summary"
argument_list|,
name|index
operator|+
name|classMarker
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|classDocs
operator|=
operator|new
name|ClassDocs
argument_list|(
name|doc
argument_list|,
name|classInfo
argument_list|)
expr_stmt|;
name|docs
operator|.
name|putIfAbsent
argument_list|(
name|resource
argument_list|,
name|classDocs
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|classDocs
return|;
block|}
specifier|private
name|MethodDocs
name|getOperationDocInternal
parameter_list|(
name|OperationResourceInfo
name|ori
parameter_list|)
throws|throws
name|Exception
block|{
name|ClassDocs
name|classDoc
init|=
name|getClassDocInternal
argument_list|(
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|classDoc
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Method
name|method
init|=
name|ori
operator|.
name|getAnnotatedMethod
argument_list|()
operator|==
literal|null
condition|?
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
else|:
name|ori
operator|.
name|getAnnotatedMethod
argument_list|()
decl_stmt|;
name|MethodDocs
name|mDocs
init|=
name|classDoc
operator|.
name|getMethodDocs
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|mDocs
operator|==
literal|null
condition|)
block|{
name|String
name|operLink
init|=
name|getOperLink
argument_list|()
decl_stmt|;
name|String
name|operMarker
init|=
name|operLink
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"("
decl_stmt|;
name|int
name|operMarkerIndex
init|=
name|classDoc
operator|.
name|getClassDoc
argument_list|()
operator|.
name|indexOf
argument_list|(
name|operMarker
argument_list|)
decl_stmt|;
while|while
condition|(
name|operMarkerIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|int
name|startOfOpSigIndex
init|=
name|operMarkerIndex
operator|+
name|operMarker
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|endOfOpSigIndex
init|=
name|classDoc
operator|.
name|getClassDoc
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|")"
argument_list|,
name|operMarkerIndex
argument_list|)
decl_stmt|;
name|int
name|paramLen
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|endOfOpSigIndex
operator|==
name|startOfOpSigIndex
operator|+
literal|1
operator|&&
name|paramLen
operator|==
literal|0
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|endOfOpSigIndex
operator|>
name|startOfOpSigIndex
operator|+
literal|1
condition|)
block|{
name|String
index|[]
name|opBits
init|=
name|classDoc
operator|.
name|getClassDoc
argument_list|()
operator|.
name|substring
argument_list|(
name|operMarkerIndex
argument_list|,
name|endOfOpSigIndex
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|opBits
operator|.
name|length
operator|==
name|paramLen
condition|)
block|{
break|break;
block|}
block|}
name|operMarkerIndex
operator|=
name|classDoc
operator|.
name|getClassDoc
argument_list|()
operator|.
name|indexOf
argument_list|(
name|operMarker
argument_list|,
name|operMarkerIndex
operator|+
name|operMarker
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|operMarkerIndex
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|operDoc
init|=
name|classDoc
operator|.
name|getClassDoc
argument_list|()
operator|.
name|substring
argument_list|(
name|operMarkerIndex
operator|+
name|operMarker
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|operInfoTag
init|=
name|getOperInfoTag
argument_list|()
decl_stmt|;
name|String
name|operInfo
init|=
name|getJavaDocText
argument_list|(
name|operDoc
argument_list|,
name|operInfoTag
argument_list|,
name|operLink
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|String
name|responseInfo
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|paramDocs
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|operInfo
argument_list|)
condition|)
block|{
name|int
name|returnsIndex
init|=
name|operDoc
operator|.
name|indexOf
argument_list|(
literal|"Returns:"
argument_list|,
name|operLink
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|nextOpIndex
init|=
name|operDoc
operator|.
name|indexOf
argument_list|(
name|operLink
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnsIndex
operator|!=
operator|-
literal|1
operator|&&
operator|(
name|nextOpIndex
operator|>
name|returnsIndex
operator|||
name|nextOpIndex
operator|==
operator|-
literal|1
operator|)
condition|)
block|{
name|responseInfo
operator|=
name|getJavaDocText
argument_list|(
name|operDoc
argument_list|,
name|getResponseMarker
argument_list|()
argument_list|,
name|operLink
argument_list|,
name|returnsIndex
operator|+
literal|8
argument_list|)
expr_stmt|;
block|}
name|int
name|paramIndex
init|=
name|operDoc
operator|.
name|indexOf
argument_list|(
literal|"Parameters:"
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramIndex
operator|!=
operator|-
literal|1
operator|&&
operator|(
name|nextOpIndex
operator|==
operator|-
literal|1
operator|||
name|paramIndex
operator|<
name|nextOpIndex
operator|)
condition|)
block|{
name|String
name|paramString
init|=
name|returnsIndex
operator|==
operator|-
literal|1
condition|?
name|operDoc
operator|.
name|substring
argument_list|(
name|paramIndex
argument_list|)
else|:
name|operDoc
operator|.
name|substring
argument_list|(
name|paramIndex
argument_list|,
name|returnsIndex
argument_list|)
decl_stmt|;
name|String
name|codeTag
init|=
name|getCodeTag
argument_list|()
decl_stmt|;
name|int
name|codeIndex
init|=
name|paramString
operator|.
name|indexOf
argument_list|(
name|codeTag
argument_list|)
decl_stmt|;
while|while
condition|(
name|codeIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|int
name|next
init|=
name|paramString
operator|.
name|indexOf
argument_list|(
literal|"<"
argument_list|,
name|codeIndex
operator|+
literal|7
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|==
operator|-
literal|1
condition|)
block|{
name|next
operator|=
name|paramString
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
name|String
name|param
init|=
name|paramString
operator|.
name|substring
argument_list|(
name|codeIndex
operator|+
literal|7
argument_list|,
name|next
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|param
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|param
operator|=
name|param
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|paramDocs
operator|.
name|add
argument_list|(
name|param
argument_list|)
expr_stmt|;
if|if
condition|(
name|next
operator|==
name|paramString
operator|.
name|length
argument_list|()
condition|)
block|{
break|break;
block|}
else|else
block|{
name|codeIndex
operator|=
name|next
operator|+
literal|1
expr_stmt|;
block|}
name|codeIndex
operator|=
name|paramString
operator|.
name|indexOf
argument_list|(
name|codeTag
argument_list|,
name|codeIndex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|mDocs
operator|=
operator|new
name|MethodDocs
argument_list|(
name|operInfo
argument_list|,
name|paramDocs
argument_list|,
name|responseInfo
argument_list|)
expr_stmt|;
name|classDoc
operator|.
name|addMethodDocs
argument_list|(
name|method
argument_list|,
name|mDocs
argument_list|)
expr_stmt|;
block|}
return|return
name|mDocs
return|;
block|}
specifier|private
name|String
name|getJavaDocText
parameter_list|(
name|String
name|doc
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|notAfterTag
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|int
name|tagIndex
init|=
name|doc
operator|.
name|indexOf
argument_list|(
name|tag
argument_list|,
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|tagIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|int
name|notAfterIndex
init|=
name|doc
operator|.
name|indexOf
argument_list|(
name|notAfterTag
argument_list|,
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|notAfterIndex
operator|==
operator|-
literal|1
operator|||
name|notAfterIndex
operator|>
name|tagIndex
condition|)
block|{
name|int
name|nextIndex
init|=
name|doc
operator|.
name|indexOf
argument_list|(
literal|"<"
argument_list|,
name|tagIndex
operator|+
name|tag
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextIndex
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|doc
operator|.
name|substring
argument_list|(
name|tagIndex
operator|+
name|tag
operator|.
name|length
argument_list|()
argument_list|,
name|nextIndex
argument_list|)
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|String
name|getClassInfoTag
parameter_list|()
block|{
if|if
condition|(
name|javaDocsBuiltByVersion
operator|==
name|JAVA_VERSION_16
condition|)
block|{
return|return
literal|"<P>"
return|;
block|}
else|else
block|{
return|return
literal|"<div class=\"block\">"
return|;
block|}
block|}
specifier|protected
name|String
name|getOperInfoTag
parameter_list|()
block|{
if|if
condition|(
name|javaDocsBuiltByVersion
operator|==
name|JAVA_VERSION_16
condition|)
block|{
return|return
literal|"<DD>"
return|;
block|}
else|else
block|{
return|return
literal|"<div class=\"block\">"
return|;
block|}
block|}
specifier|protected
name|String
name|getOperLink
parameter_list|()
block|{
name|String
name|operLink
init|=
literal|"<A NAME=\""
decl_stmt|;
return|return
name|javaDocsBuiltByVersion
operator|==
name|JAVA_VERSION_16
condition|?
name|operLink
else|:
name|operLink
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getResponseMarker
parameter_list|()
block|{
name|String
name|tag
init|=
literal|"<DD>"
decl_stmt|;
return|return
name|javaDocsBuiltByVersion
operator|==
name|JAVA_VERSION_16
condition|?
name|tag
else|:
name|tag
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getCodeTag
parameter_list|()
block|{
name|String
name|tag
init|=
literal|"</CODE>"
decl_stmt|;
return|return
name|javaDocsBuiltByVersion
operator|==
name|JAVA_VERSION_16
condition|?
name|tag
else|:
name|tag
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
specifier|public
name|void
name|setJavaDocsBuiltByVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|javaDocsBuiltByVersion
operator|=
name|Double
operator|.
name|valueOf
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|ClassDocs
block|{
specifier|private
name|String
name|classDoc
decl_stmt|;
specifier|private
name|String
name|classInfo
decl_stmt|;
specifier|private
name|ConcurrentHashMap
argument_list|<
name|Method
argument_list|,
name|MethodDocs
argument_list|>
name|mdocs
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Method
argument_list|,
name|MethodDocs
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ClassDocs
parameter_list|(
name|String
name|classDoc
parameter_list|,
name|String
name|classInfo
parameter_list|)
block|{
name|this
operator|.
name|classDoc
operator|=
name|classDoc
expr_stmt|;
name|this
operator|.
name|classInfo
operator|=
name|classInfo
expr_stmt|;
block|}
specifier|public
name|String
name|getClassDoc
parameter_list|()
block|{
return|return
name|classDoc
return|;
block|}
specifier|public
name|String
name|getClassInfo
parameter_list|()
block|{
return|return
name|classInfo
return|;
block|}
specifier|public
name|MethodDocs
name|getMethodDocs
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
return|return
name|mdocs
operator|.
name|get
argument_list|(
name|method
argument_list|)
return|;
block|}
specifier|public
name|void
name|addMethodDocs
parameter_list|(
name|Method
name|method
parameter_list|,
name|MethodDocs
name|doc
parameter_list|)
block|{
name|mdocs
operator|.
name|putIfAbsent
argument_list|(
name|method
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|MethodDocs
block|{
specifier|private
name|String
name|methodInfo
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|paramInfo
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|responseInfo
decl_stmt|;
specifier|public
name|MethodDocs
parameter_list|(
name|String
name|methodInfo
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|paramInfo
parameter_list|,
name|String
name|responseInfo
parameter_list|)
block|{
name|this
operator|.
name|methodInfo
operator|=
name|methodInfo
expr_stmt|;
name|this
operator|.
name|paramInfo
operator|=
name|paramInfo
expr_stmt|;
name|this
operator|.
name|responseInfo
operator|=
name|responseInfo
expr_stmt|;
block|}
specifier|public
name|String
name|getMethodInfo
parameter_list|()
block|{
return|return
name|methodInfo
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getParamInfo
parameter_list|()
block|{
return|return
name|paramInfo
return|;
block|}
specifier|public
name|String
name|getResponseInfo
parameter_list|()
block|{
return|return
name|responseInfo
return|;
block|}
block|}
block|}
end_class

end_unit

