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
name|cdi
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
name|ParameterizedType
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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|RequestScoped
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|spi
operator|.
name|CreationalContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|PassivationCapable
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
name|message
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
name|phase
operator|.
name|PhaseInterceptorChain
import|;
end_import

begin_class
specifier|public
class|class
name|ContextProducerBean
extends|extends
name|AbstractCXFBean
argument_list|<
name|Object
argument_list|>
implements|implements
name|PassivationCapable
block|{
specifier|private
specifier|final
name|Type
name|type
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|Annotation
argument_list|>
name|qualifiers
decl_stmt|;
name|ContextProducerBean
parameter_list|(
name|Type
name|type
parameter_list|,
name|boolean
name|defaultQualifier
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|qualifiers
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|defaultQualifier
condition|?
literal|2
else|:
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|qualifiers
operator|.
name|add
argument_list|(
name|ContextResolved
operator|.
name|LITERAL
argument_list|)
expr_stmt|;
if|if
condition|(
name|defaultQualifier
condition|)
block|{
name|this
operator|.
name|qualifiers
operator|.
name|add
argument_list|(
name|DEFAULT
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Annotation
argument_list|>
name|getQualifiers
parameter_list|()
block|{
return|return
name|qualifiers
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|getScope
parameter_list|()
block|{
return|return
name|RequestScoped
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getBeanClass
parameter_list|()
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|type
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|create
parameter_list|(
name|CreationalContext
argument_list|<
name|Object
argument_list|>
name|creationalContext
parameter_list|)
block|{
return|return
name|createContextValue
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isNullable
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Type
argument_list|>
name|getTypes
parameter_list|()
block|{
name|Set
argument_list|<
name|Type
argument_list|>
name|types
init|=
name|super
operator|.
name|getTypes
argument_list|()
decl_stmt|;
name|types
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|types
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"CxfContextProducer"
operator|+
name|type
return|;
block|}
specifier|private
name|Object
name|createContextValue
parameter_list|()
block|{
name|Message
name|currentMessage
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|Type
name|genericType
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|contextType
decl_stmt|;
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|parameterizedType
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
name|genericType
operator|=
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
name|contextType
operator|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|parameterizedType
operator|.
name|getRawType
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|contextType
operator|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|type
expr_stmt|;
block|}
return|return
name|JAXRSUtils
operator|.
name|createContextValue
argument_list|(
name|currentMessage
argument_list|,
name|genericType
argument_list|,
name|contextType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

