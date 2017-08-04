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
name|io
operator|.
name|InputStream
import|;
end_import

begin_class
specifier|public
class|class
name|Parameter
block|{
specifier|private
name|ParameterType
name|type
decl_stmt|;
specifier|private
name|int
name|ind
decl_stmt|;
specifier|private
name|String
name|aValue
decl_stmt|;
specifier|private
name|boolean
name|isEncoded
decl_stmt|;
specifier|private
name|String
name|defaultValue
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|javaType
decl_stmt|;
specifier|public
name|Parameter
parameter_list|()
block|{      }
specifier|public
name|Parameter
parameter_list|(
name|String
name|type
parameter_list|,
name|int
name|pos
parameter_list|,
name|String
name|aValue
parameter_list|)
block|{
name|this
argument_list|(
name|ParameterType
operator|.
name|valueOf
argument_list|(
name|type
argument_list|)
argument_list|,
name|pos
argument_list|,
name|aValue
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Parameter
parameter_list|(
name|ParameterType
name|type
parameter_list|,
name|String
name|aValue
parameter_list|)
block|{
name|this
argument_list|(
name|type
argument_list|,
literal|0
argument_list|,
name|aValue
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Parameter
parameter_list|(
name|ParameterType
name|type
parameter_list|,
name|int
name|ind
parameter_list|,
name|String
name|aValue
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
name|ind
operator|=
name|ind
expr_stmt|;
name|this
operator|.
name|aValue
operator|=
name|aValue
expr_stmt|;
block|}
specifier|public
name|Parameter
parameter_list|(
name|ParameterType
name|type
parameter_list|,
name|int
name|ind
parameter_list|,
name|String
name|aValue
parameter_list|,
name|boolean
name|isEncoded
parameter_list|,
name|String
name|defaultValue
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
name|ind
operator|=
name|ind
expr_stmt|;
name|this
operator|.
name|aValue
operator|=
name|aValue
expr_stmt|;
name|this
operator|.
name|isEncoded
operator|=
name|isEncoded
expr_stmt|;
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
block|}
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|ind
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|aValue
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|aValue
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|ParameterType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|stype
parameter_list|)
block|{
name|type
operator|=
name|ParameterType
operator|.
name|valueOf
argument_list|(
name|stype
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEncoded
parameter_list|()
block|{
return|return
name|isEncoded
return|;
block|}
specifier|public
name|void
name|setEncoded
parameter_list|(
name|boolean
name|encoded
parameter_list|)
block|{
name|isEncoded
operator|=
name|encoded
expr_stmt|;
block|}
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
name|defaultValue
return|;
block|}
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|dValue
parameter_list|)
block|{
name|defaultValue
operator|=
name|dValue
expr_stmt|;
block|}
specifier|public
name|void
name|setJavaType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|jType
parameter_list|)
block|{
name|this
operator|.
name|javaType
operator|=
name|jType
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getJavaType
parameter_list|()
block|{
if|if
condition|(
name|javaType
operator|==
literal|null
condition|)
block|{
return|return
name|type
operator|==
name|ParameterType
operator|.
name|REQUEST_BODY
condition|?
name|InputStream
operator|.
name|class
else|:
name|String
operator|.
name|class
return|;
block|}
return|return
name|javaType
return|;
block|}
block|}
end_class

end_unit

