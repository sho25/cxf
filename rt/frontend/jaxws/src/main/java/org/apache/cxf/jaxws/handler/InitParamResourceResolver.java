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
name|jaxws
operator|.
name|handler
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
name|util
operator|.
name|Map
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
name|resource
operator|.
name|ResourceResolver
import|;
end_import

begin_class
specifier|public
class|class
name|InitParamResourceResolver
implements|implements
name|ResourceResolver
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
name|InitParamResourceResolver
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
decl_stmt|;
specifier|public
name|InitParamResourceResolver
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|params
operator|=
name|map
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|resolve
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resourceType
parameter_list|)
block|{
name|String
name|value
init|=
name|params
operator|.
name|get
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
return|return
name|convertToType
argument_list|(
name|value
argument_list|,
name|resourceType
argument_list|)
return|;
block|}
specifier|public
name|InputStream
name|getAsStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// returning these as a stream does not make much sense
return|return
literal|null
return|;
block|}
comment|/**      * Convert the string representation of value to type T      */
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|convertToType
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
comment|/*         char, byte, short, long, float, double, boolean         */
name|T
name|ret
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|String
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
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Integer
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Integer
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|type
operator|.
name|cast
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Byte
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Byte
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|type
operator|.
name|cast
argument_list|(
name|Byte
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Short
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Short
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|type
operator|.
name|cast
argument_list|(
name|Short
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Long
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Long
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|type
operator|.
name|cast
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Float
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Float
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|type
operator|.
name|cast
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Double
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Double
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|type
operator|.
name|cast
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Boolean
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Boolean
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|type
operator|.
name|cast
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Character
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|Character
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ret
operator|=
name|type
operator|.
name|cast
argument_list|(
name|value
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"do not know how to treat type: "
operator|+
name|type
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"badly formed init param: "
operator|+
name|value
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

