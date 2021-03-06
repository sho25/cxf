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
name|systest
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tests
operator|.
name|inherit
operator|.
name|Inherit
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
name|tests
operator|.
name|inherit
operator|.
name|objects
operator|.
name|BaseType
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
name|tests
operator|.
name|inherit
operator|.
name|objects
operator|.
name|SubTypeA
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
name|tests
operator|.
name|inherit
operator|.
name|objects
operator|.
name|SubTypeB
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
name|tests
operator|.
name|inherit
operator|.
name|types
operator|.
name|ObjectInfo
import|;
end_import

begin_class
specifier|public
class|class
name|InheritImpl
implements|implements
name|Inherit
block|{
specifier|public
name|ObjectInfo
name|getObject
parameter_list|(
name|int
name|type
parameter_list|)
block|{
name|ObjectInfo
name|info
init|=
operator|new
name|ObjectInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|setType
argument_list|(
literal|"Type: "
operator|+
name|type
argument_list|)
expr_stmt|;
name|BaseType
name|ba
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
literal|0
case|:
name|ba
operator|=
operator|new
name|SubTypeA
argument_list|()
expr_stmt|;
name|ba
operator|.
name|setName
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SubTypeA
operator|)
name|ba
operator|)
operator|.
name|setAvalue
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|ba
operator|=
operator|new
name|SubTypeB
argument_list|()
expr_stmt|;
name|ba
operator|.
name|setName
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SubTypeB
operator|)
name|ba
operator|)
operator|.
name|setBvalue
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
break|break;
default|default:
block|}
name|info
operator|.
name|setBaseObject
argument_list|(
name|ba
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
block|}
end_class

end_unit

