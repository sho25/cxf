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
name|java2wsdl
operator|.
name|processor
package|;
end_package

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
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
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
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceProvider
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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
operator|.
name|WrapperUtil
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
name|util
operator|.
name|AnnotationUtil
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|FrontendFactory
block|{
specifier|private
specifier|static
name|FrontendFactory
name|instance
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Method
argument_list|>
name|wsMethods
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
index|[]
name|annotations
init|=
operator|new
name|Class
index|[]
block|{
name|SOAPBinding
operator|.
name|class
block|,
name|WebService
operator|.
name|class
block|,
name|WebServiceProvider
operator|.
name|class
block|}
decl_stmt|;
specifier|public
enum|enum
name|Style
block|{
name|Jaxws
block|,
name|Simple
block|}
specifier|private
name|FrontendFactory
parameter_list|()
block|{     }
specifier|public
specifier|static
name|FrontendFactory
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|FrontendFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
specifier|private
name|boolean
name|isJaxws
parameter_list|()
block|{
if|if
condition|(
name|serviceClass
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|serviceClass
operator|.
name|isAnnotationPresent
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
if|if
condition|(
name|isJAXWSAnnotationExists
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|isJAXWSAnnotationExists
parameter_list|()
block|{
for|for
control|(
name|Method
name|method
range|:
name|wsMethods
control|)
block|{
if|if
condition|(
name|WrapperUtil
operator|.
name|isWrapperClassExists
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|WebMethod
name|m
init|=
name|AnnotationUtil
operator|.
name|getPrivMethodAnnotation
argument_list|(
name|method
argument_list|,
name|WebMethod
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|WebResult
name|res
init|=
name|AnnotationUtil
operator|.
name|getPrivMethodAnnotation
argument_list|(
name|method
argument_list|,
name|WebResult
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|List
argument_list|<
name|Method
argument_list|>
name|getWSMethods
parameter_list|()
block|{
name|List
argument_list|<
name|Method
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|serviceClass
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
operator|||
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|||
name|isExcluced
argument_list|(
name|method
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|methods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
return|return
name|methods
return|;
block|}
specifier|private
name|boolean
name|isExcluced
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|WebMethod
name|webMethod
init|=
name|AnnotationUtil
operator|.
name|getPrivMethodAnnotation
argument_list|(
name|method
argument_list|,
name|WebMethod
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|webMethod
operator|!=
literal|null
operator|&&
name|webMethod
operator|.
name|exclude
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|Style
name|discoverStyle
parameter_list|()
block|{
if|if
condition|(
name|isJaxws
argument_list|()
condition|)
block|{
return|return
name|Style
operator|.
name|Jaxws
return|;
block|}
return|return
name|Style
operator|.
name|Simple
return|;
block|}
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|this
operator|.
name|serviceClass
operator|=
name|c
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|wsMethods
operator|=
name|getWSMethods
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

