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
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_class
specifier|public
class|class
name|UserOperation
block|{
specifier|private
name|String
name|methodName
decl_stmt|;
specifier|private
name|String
name|httpMethodName
decl_stmt|;
specifier|private
name|String
name|pathValue
decl_stmt|;
specifier|private
name|String
name|consumesTypes
decl_stmt|;
specifier|private
name|String
name|producesTypes
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Parameter
argument_list|>
name|params
decl_stmt|;
specifier|public
name|UserOperation
parameter_list|()
block|{              }
specifier|public
name|UserOperation
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
argument_list|(
name|methodName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|UserOperation
parameter_list|(
name|String
name|methodName
parameter_list|,
name|String
name|pathValue
parameter_list|)
block|{
name|this
argument_list|(
name|methodName
argument_list|,
name|pathValue
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|UserOperation
parameter_list|(
name|String
name|methodName
parameter_list|,
name|String
name|pathValue
parameter_list|,
name|List
argument_list|<
name|Parameter
argument_list|>
name|ps
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
name|this
operator|.
name|pathValue
operator|=
name|pathValue
expr_stmt|;
name|this
operator|.
name|params
operator|=
name|ps
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|methodName
operator|=
name|name
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getVerb
parameter_list|()
block|{
return|return
name|httpMethodName
return|;
block|}
specifier|public
name|void
name|setVerb
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|httpMethodName
operator|=
name|name
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getConsumes
parameter_list|()
block|{
return|return
name|consumesTypes
operator|==
literal|null
condition|?
name|MediaType
operator|.
name|APPLICATION_XML
else|:
name|consumesTypes
return|;
block|}
specifier|public
name|String
name|getProduces
parameter_list|()
block|{
return|return
name|producesTypes
operator|==
literal|null
condition|?
name|MediaType
operator|.
name|APPLICATION_XML
else|:
name|producesTypes
return|;
block|}
specifier|public
name|void
name|setConsumes
parameter_list|(
name|String
name|types
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|types
argument_list|)
condition|)
block|{
name|consumesTypes
operator|=
name|types
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setProduces
parameter_list|(
name|String
name|types
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|types
argument_list|)
condition|)
block|{
name|producesTypes
operator|=
name|types
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|pathValue
operator|==
literal|null
condition|?
literal|"/"
else|:
name|pathValue
return|;
block|}
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|pathValue
operator|=
name|path
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setParameters
parameter_list|(
name|List
argument_list|<
name|Parameter
argument_list|>
name|ps
parameter_list|)
block|{
name|params
operator|=
name|ps
expr_stmt|;
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
name|params
operator|==
literal|null
condition|?
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|Parameter
operator|.
name|class
argument_list|)
else|:
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|params
argument_list|)
return|;
block|}
block|}
end_class

end_unit

