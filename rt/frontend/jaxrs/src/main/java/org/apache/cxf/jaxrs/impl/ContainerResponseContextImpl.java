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
name|io
operator|.
name|OutputStream
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
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|container
operator|.
name|ContainerResponseContext
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
name|MultivaluedMap
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
name|InjectionUtils
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
name|ContainerResponseContextImpl
extends|extends
name|AbstractResponseContextImpl
implements|implements
name|ContainerResponseContext
block|{
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|serviceCls
decl_stmt|;
specifier|private
name|Method
name|invoked
decl_stmt|;
specifier|public
name|ContainerResponseContextImpl
parameter_list|(
name|ResponseImpl
name|r
parameter_list|,
name|Message
name|m
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|serviceCls
parameter_list|,
name|Method
name|invoked
parameter_list|)
block|{
name|super
argument_list|(
name|r
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|this
operator|.
name|serviceCls
operator|=
name|serviceCls
expr_stmt|;
name|this
operator|.
name|invoked
operator|=
name|invoked
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Annotation
index|[]
name|getEntityAnnotations
parameter_list|()
block|{
return|return
name|super
operator|.
name|getResponseEntityAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getEntityClass
parameter_list|()
block|{
return|return
name|InjectionUtils
operator|.
name|getRawResponseClass
argument_list|(
name|super
operator|.
name|r
operator|.
name|getActualEntity
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Type
name|getEntityType
parameter_list|()
block|{
return|return
name|InjectionUtils
operator|.
name|getGenericResponseType
argument_list|(
name|invoked
argument_list|,
name|serviceCls
argument_list|,
name|super
operator|.
name|r
operator|.
name|getActualEntity
argument_list|()
argument_list|,
name|getEntityClass
argument_list|()
argument_list|,
name|super
operator|.
name|m
operator|.
name|getExchange
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|r
operator|.
name|getMetadata
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|OutputStream
name|getEntityStream
parameter_list|()
block|{
return|return
name|m
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setEntityStream
parameter_list|(
name|OutputStream
name|os
parameter_list|)
block|{
name|m
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

