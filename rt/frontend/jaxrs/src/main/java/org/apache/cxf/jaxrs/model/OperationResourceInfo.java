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
package|;
end_package

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
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
name|DefaultValue
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
name|Encoded
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
name|Produces
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
name|core
operator|.
name|MediaType
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
name|ext
operator|.
name|Oneway
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
name|AnnotationUtils
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
name|JAXRSUtils
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
name|OperationResourceInfo
block|{
specifier|private
name|URITemplate
name|uriTemplate
decl_stmt|;
specifier|private
name|ClassResourceInfo
name|classResourceInfo
decl_stmt|;
specifier|private
name|Method
name|methodToInvoke
decl_stmt|;
specifier|private
name|Method
name|annotatedMethod
decl_stmt|;
specifier|private
name|String
name|httpMethod
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MediaType
argument_list|>
name|produceMimes
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MediaType
argument_list|>
name|consumeMimes
decl_stmt|;
specifier|private
name|boolean
name|encoded
decl_stmt|;
specifier|private
name|String
name|defaultParamValue
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Parameter
argument_list|>
name|parameters
decl_stmt|;
specifier|private
name|boolean
name|oneway
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|nameBindings
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|public
name|OperationResourceInfo
parameter_list|(
name|Method
name|mInvoke
parameter_list|,
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|this
argument_list|(
name|mInvoke
argument_list|,
name|mInvoke
argument_list|,
name|cri
argument_list|)
expr_stmt|;
block|}
name|OperationResourceInfo
parameter_list|(
name|OperationResourceInfo
name|ori
parameter_list|,
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|this
operator|.
name|uriTemplate
operator|=
name|ori
operator|.
name|uriTemplate
expr_stmt|;
name|this
operator|.
name|methodToInvoke
operator|=
name|ori
operator|.
name|methodToInvoke
expr_stmt|;
name|this
operator|.
name|annotatedMethod
operator|=
name|ori
operator|.
name|annotatedMethod
expr_stmt|;
name|this
operator|.
name|httpMethod
operator|=
name|ori
operator|.
name|httpMethod
expr_stmt|;
name|this
operator|.
name|produceMimes
operator|=
name|ori
operator|.
name|produceMimes
expr_stmt|;
name|this
operator|.
name|consumeMimes
operator|=
name|ori
operator|.
name|consumeMimes
expr_stmt|;
name|this
operator|.
name|encoded
operator|=
name|ori
operator|.
name|encoded
expr_stmt|;
name|this
operator|.
name|defaultParamValue
operator|=
name|ori
operator|.
name|defaultParamValue
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|ori
operator|.
name|parameters
expr_stmt|;
name|this
operator|.
name|oneway
operator|=
name|ori
operator|.
name|oneway
expr_stmt|;
name|this
operator|.
name|classResourceInfo
operator|=
name|cri
expr_stmt|;
name|this
operator|.
name|nameBindings
operator|=
name|ori
operator|.
name|nameBindings
expr_stmt|;
block|}
specifier|public
name|OperationResourceInfo
parameter_list|(
name|Method
name|mInvoke
parameter_list|,
name|Method
name|mAnnotated
parameter_list|,
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|methodToInvoke
operator|=
name|mInvoke
expr_stmt|;
name|annotatedMethod
operator|=
name|mAnnotated
expr_stmt|;
if|if
condition|(
name|mAnnotated
operator|!=
literal|null
condition|)
block|{
name|parameters
operator|=
name|ResourceUtils
operator|.
name|getParameters
argument_list|(
name|mAnnotated
argument_list|)
expr_stmt|;
name|nameBindings
operator|=
name|AnnotationUtils
operator|.
name|getNameBindings
argument_list|(
name|mAnnotated
operator|.
name|getAnnotations
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|classResourceInfo
operator|=
name|cri
expr_stmt|;
name|checkMediaTypes
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|checkEncoded
argument_list|()
expr_stmt|;
name|checkDefaultParameterValue
argument_list|()
expr_stmt|;
name|checkOneway
argument_list|()
expr_stmt|;
block|}
comment|//CHECKSTYLE:OFF
specifier|public
name|OperationResourceInfo
parameter_list|(
name|Method
name|m
parameter_list|,
name|ClassResourceInfo
name|cri
parameter_list|,
name|URITemplate
name|template
parameter_list|,
name|String
name|httpVerb
parameter_list|,
name|String
name|consumeMediaTypes
parameter_list|,
name|String
name|produceMediaTypes
parameter_list|,
name|List
argument_list|<
name|Parameter
argument_list|>
name|params
parameter_list|,
name|boolean
name|oneway
parameter_list|)
block|{
comment|//CHECKSTYLE:ON
name|methodToInvoke
operator|=
name|m
expr_stmt|;
name|annotatedMethod
operator|=
literal|null
expr_stmt|;
name|classResourceInfo
operator|=
name|cri
expr_stmt|;
name|uriTemplate
operator|=
name|template
expr_stmt|;
name|httpMethod
operator|=
name|httpVerb
expr_stmt|;
name|checkMediaTypes
argument_list|(
name|consumeMediaTypes
argument_list|,
name|produceMediaTypes
argument_list|)
expr_stmt|;
name|parameters
operator|=
name|params
expr_stmt|;
name|this
operator|.
name|oneway
operator|=
name|oneway
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getNameBindings
parameter_list|()
block|{
return|return
name|nameBindings
return|;
block|}
specifier|private
name|void
name|checkOneway
parameter_list|()
block|{
if|if
condition|(
name|annotatedMethod
operator|!=
literal|null
condition|)
block|{
name|oneway
operator|=
name|AnnotationUtils
operator|.
name|getAnnotation
argument_list|(
name|annotatedMethod
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|Oneway
operator|.
name|class
argument_list|)
operator|!=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isOneway
parameter_list|()
block|{
return|return
name|oneway
return|;
block|}
specifier|public
name|List
argument_list|<
name|Parameter
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
specifier|public
name|URITemplate
name|getURITemplate
parameter_list|()
block|{
return|return
name|uriTemplate
return|;
block|}
specifier|public
name|void
name|setURITemplate
parameter_list|(
name|URITemplate
name|u
parameter_list|)
block|{
name|uriTemplate
operator|=
name|u
expr_stmt|;
block|}
specifier|public
name|ClassResourceInfo
name|getClassResourceInfo
parameter_list|()
block|{
return|return
name|classResourceInfo
return|;
block|}
specifier|public
name|Method
name|getMethodToInvoke
parameter_list|()
block|{
return|return
name|methodToInvoke
return|;
block|}
specifier|public
name|Method
name|getAnnotatedMethod
parameter_list|()
block|{
return|return
name|annotatedMethod
return|;
block|}
specifier|public
name|void
name|setMethodToInvoke
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
name|methodToInvoke
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|String
name|getHttpMethod
parameter_list|()
block|{
return|return
name|httpMethod
return|;
block|}
specifier|public
name|void
name|setHttpMethod
parameter_list|(
name|String
name|m
parameter_list|)
block|{
name|httpMethod
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSubResourceLocator
parameter_list|()
block|{
return|return
name|httpMethod
operator|==
literal|null
condition|?
literal|true
else|:
literal|false
return|;
block|}
specifier|public
name|List
argument_list|<
name|MediaType
argument_list|>
name|getProduceTypes
parameter_list|()
block|{
return|return
name|produceMimes
return|;
block|}
specifier|public
name|List
argument_list|<
name|MediaType
argument_list|>
name|getConsumeTypes
parameter_list|()
block|{
return|return
name|consumeMimes
return|;
block|}
specifier|private
name|void
name|checkMediaTypes
parameter_list|(
name|String
name|consumeMediaTypes
parameter_list|,
name|String
name|produceMediaTypes
parameter_list|)
block|{
if|if
condition|(
name|consumeMediaTypes
operator|!=
literal|null
condition|)
block|{
name|consumeMimes
operator|=
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|consumeMediaTypes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Consumes
name|cm
init|=
name|AnnotationUtils
operator|.
name|getMethodAnnotation
argument_list|(
name|annotatedMethod
argument_list|,
name|Consumes
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cm
operator|!=
literal|null
condition|)
block|{
name|consumeMimes
operator|=
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|JAXRSUtils
operator|.
name|getMediaTypes
argument_list|(
name|cm
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|classResourceInfo
operator|!=
literal|null
condition|)
block|{
name|consumeMimes
operator|=
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|classResourceInfo
operator|.
name|getConsumeMime
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|produceMediaTypes
operator|!=
literal|null
condition|)
block|{
name|produceMimes
operator|=
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|produceMediaTypes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Produces
name|pm
init|=
name|AnnotationUtils
operator|.
name|getMethodAnnotation
argument_list|(
name|annotatedMethod
argument_list|,
name|Produces
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|pm
operator|!=
literal|null
condition|)
block|{
name|produceMimes
operator|=
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|JAXRSUtils
operator|.
name|getMediaTypes
argument_list|(
name|pm
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|classResourceInfo
operator|!=
literal|null
condition|)
block|{
name|produceMimes
operator|=
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|classResourceInfo
operator|.
name|getProduceMime
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isEncodedEnabled
parameter_list|()
block|{
return|return
name|encoded
return|;
block|}
specifier|public
name|String
name|getDefaultParameterValue
parameter_list|()
block|{
return|return
name|defaultParamValue
return|;
block|}
specifier|private
name|void
name|checkEncoded
parameter_list|()
block|{
name|encoded
operator|=
name|AnnotationUtils
operator|.
name|getMethodAnnotation
argument_list|(
name|annotatedMethod
argument_list|,
name|Encoded
operator|.
name|class
argument_list|)
operator|!=
literal|null
expr_stmt|;
if|if
condition|(
operator|!
name|encoded
operator|&&
name|classResourceInfo
operator|!=
literal|null
condition|)
block|{
name|encoded
operator|=
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|classResourceInfo
operator|.
name|getServiceClass
argument_list|()
argument_list|,
name|Encoded
operator|.
name|class
argument_list|)
operator|!=
literal|null
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkDefaultParameterValue
parameter_list|()
block|{
name|DefaultValue
name|dv
init|=
name|AnnotationUtils
operator|.
name|getMethodAnnotation
argument_list|(
name|annotatedMethod
argument_list|,
name|DefaultValue
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dv
operator|==
literal|null
operator|&&
name|classResourceInfo
operator|!=
literal|null
condition|)
block|{
name|dv
operator|=
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|classResourceInfo
operator|.
name|getServiceClass
argument_list|()
argument_list|,
name|DefaultValue
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dv
operator|!=
literal|null
condition|)
block|{
name|defaultParamValue
operator|=
name|dv
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

