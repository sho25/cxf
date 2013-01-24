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
name|ws
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
name|Arrays
import|;
end_import

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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|attachment
operator|.
name|external
operator|.
name|DomainExpression
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
name|attachment
operator|.
name|external
operator|.
name|DomainExpressionBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|URIDomainExpressionBuilder
implements|implements
name|DomainExpressionBuilder
block|{
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|QName
argument_list|>
name|SUPPORTED_TYPES
init|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|QName
index|[]
block|{
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/ns/ws-policy"
argument_list|,
literal|"URI"
argument_list|)
block|,
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2004/09/policy"
argument_list|,
literal|"URI"
argument_list|)
block|}
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|DomainExpression
name|build
parameter_list|(
name|Element
name|paramElement
parameter_list|)
block|{
return|return
operator|new
name|UriDomainExpression
argument_list|(
name|paramElement
operator|.
name|getTextContent
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|QName
argument_list|>
name|getDomainExpressionTypes
parameter_list|()
block|{
return|return
name|SUPPORTED_TYPES
return|;
block|}
block|}
end_class

end_unit

