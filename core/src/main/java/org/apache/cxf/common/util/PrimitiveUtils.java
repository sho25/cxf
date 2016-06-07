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
name|common
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|PrimitiveUtils
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|AUTOBOXED_PRIMITIVES_MAP
decl_stmt|;
static|static
block|{
name|AUTOBOXED_PRIMITIVES_MAP
operator|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|AUTOBOXED_PRIMITIVES_MAP
operator|.
name|put
argument_list|(
name|byte
operator|.
name|class
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
expr_stmt|;
name|AUTOBOXED_PRIMITIVES_MAP
operator|.
name|put
argument_list|(
name|short
operator|.
name|class
argument_list|,
name|Short
operator|.
name|class
argument_list|)
expr_stmt|;
name|AUTOBOXED_PRIMITIVES_MAP
operator|.
name|put
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
expr_stmt|;
name|AUTOBOXED_PRIMITIVES_MAP
operator|.
name|put
argument_list|(
name|long
operator|.
name|class
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|AUTOBOXED_PRIMITIVES_MAP
operator|.
name|put
argument_list|(
name|float
operator|.
name|class
argument_list|,
name|Float
operator|.
name|class
argument_list|)
expr_stmt|;
name|AUTOBOXED_PRIMITIVES_MAP
operator|.
name|put
argument_list|(
name|double
operator|.
name|class
argument_list|,
name|Double
operator|.
name|class
argument_list|)
expr_stmt|;
name|AUTOBOXED_PRIMITIVES_MAP
operator|.
name|put
argument_list|(
name|boolean
operator|.
name|class
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|PrimitiveUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|boolean
name|canPrimitiveTypeBeAutoboxed
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|primitiveClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|primitiveClass
operator|.
name|isPrimitive
argument_list|()
operator|&&
name|type
operator|==
name|AUTOBOXED_PRIMITIVES_MAP
operator|.
name|get
argument_list|(
name|primitiveClass
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getClass
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"int"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|clz
operator|=
name|int
operator|.
name|class
expr_stmt|;
block|}
if|if
condition|(
literal|"byte"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|clz
operator|=
name|byte
operator|.
name|class
expr_stmt|;
block|}
if|if
condition|(
literal|"short"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|clz
operator|=
name|short
operator|.
name|class
expr_stmt|;
block|}
if|if
condition|(
literal|"long"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|clz
operator|=
name|long
operator|.
name|class
expr_stmt|;
block|}
if|if
condition|(
literal|"float"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|clz
operator|=
name|float
operator|.
name|class
expr_stmt|;
block|}
if|if
condition|(
literal|"double"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|clz
operator|=
name|double
operator|.
name|class
expr_stmt|;
block|}
if|if
condition|(
literal|"boolean"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|clz
operator|=
name|boolean
operator|.
name|class
expr_stmt|;
block|}
if|if
condition|(
literal|"char"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|clz
operator|=
name|char
operator|.
name|class
expr_stmt|;
block|}
return|return
name|clz
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Object
name|read
parameter_list|(
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|ret
init|=
name|value
decl_stmt|;
if|if
condition|(
name|Integer
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Integer
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Byte
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Byte
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|Byte
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Short
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Short
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|Short
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Long
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Long
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|Long
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Float
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Float
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|Float
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Double
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Double
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|Double
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Boolean
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Boolean
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Character
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Character
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|value
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

