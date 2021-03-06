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
name|impl
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
name|Type
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
name|provider
operator|.
name|ProviderFactory
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractInterceptorContextImpl
extends|extends
name|AbstractPropertiesImpl
block|{
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
specifier|private
name|Type
name|type
decl_stmt|;
specifier|private
name|Annotation
index|[]
name|anns
decl_stmt|;
specifier|public
name|AbstractInterceptorContextImpl
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|type
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|cls
operator|=
name|cls
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|anns
operator|=
name|anns
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|cls
return|;
block|}
specifier|public
name|Annotation
index|[]
name|getAnnotations
parameter_list|()
block|{
return|return
name|anns
return|;
block|}
specifier|public
name|Type
name|getGenericType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setAnnotations
parameter_list|(
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
if|if
condition|(
name|annotations
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
name|anns
operator|=
name|annotations
expr_stmt|;
block|}
specifier|public
name|void
name|setGenericType
parameter_list|(
name|Type
name|genType
parameter_list|)
block|{
name|type
operator|=
name|genType
expr_stmt|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|ctype
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|!=
literal|null
operator|&&
operator|!
name|cls
operator|.
name|isAssignableFrom
argument_list|(
name|ctype
argument_list|)
condition|)
block|{
name|providerSelectionPropertyChanged
argument_list|()
expr_stmt|;
block|}
name|cls
operator|=
name|ctype
expr_stmt|;
block|}
specifier|protected
name|void
name|providerSelectionPropertyChanged
parameter_list|()
block|{
name|m
operator|.
name|put
argument_list|(
name|ProviderFactory
operator|.
name|PROVIDER_SELECTION_PROPERTY_CHANGED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

