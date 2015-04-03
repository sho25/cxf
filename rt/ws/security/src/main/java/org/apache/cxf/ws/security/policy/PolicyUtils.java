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
name|ws
operator|.
name|security
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|HashSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|ws
operator|.
name|policy
operator|.
name|AssertionInfo
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
name|ws
operator|.
name|policy
operator|.
name|AssertionInfoMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SP11Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SP12Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SPConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|AbstractBinding
import|;
end_import

begin_comment
comment|/**  * Some common functionality that can be shared for working with policies  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PolicyUtils
block|{
specifier|private
name|PolicyUtils
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
specifier|static
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|getAllAssertionsByLocalname
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|localname
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp11Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp12Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|sp11Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp11Ais
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|sp12Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp12Ais
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|sp11Ais
operator|!=
literal|null
condition|)
block|{
name|ais
operator|.
name|addAll
argument_list|(
name|sp11Ais
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sp12Ais
operator|!=
literal|null
condition|)
block|{
name|ais
operator|.
name|addAll
argument_list|(
name|sp12Ais
argument_list|)
expr_stmt|;
block|}
return|return
name|ais
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|boolean
name|assertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|boolean
name|assertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|localname
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|localname
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|AssertionInfo
name|getFirstAssertionByLocalname
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|localname
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp11Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|sp11Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp11Ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|sp11Ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp12Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|sp12Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp12Ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|sp12Ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isThereAnAssertionByLocalname
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|localname
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp11Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp12Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|sp11Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp11Ais
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|sp12Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp12Ais
operator|.
name|isEmpty
argument_list|()
operator|)
return|;
block|}
specifier|public
specifier|static
name|AbstractBinding
name|getSecurityBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|AssertionInfo
name|transAis
init|=
name|PolicyUtils
operator|.
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|TRANSPORT_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|transAis
operator|!=
literal|null
condition|)
block|{
name|transAis
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|(
name|AbstractBinding
operator|)
name|transAis
operator|.
name|getAssertion
argument_list|()
return|;
block|}
name|AssertionInfo
name|asymAis
init|=
name|PolicyUtils
operator|.
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|asymAis
operator|!=
literal|null
condition|)
block|{
name|asymAis
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|(
name|AbstractBinding
operator|)
name|asymAis
operator|.
name|getAssertion
argument_list|()
return|;
block|}
name|AssertionInfo
name|symAis
init|=
name|PolicyUtils
operator|.
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SYMMETRIC_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|symAis
operator|!=
literal|null
condition|)
block|{
name|symAis
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|(
name|AbstractBinding
operator|)
name|symAis
operator|.
name|getAssertion
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

