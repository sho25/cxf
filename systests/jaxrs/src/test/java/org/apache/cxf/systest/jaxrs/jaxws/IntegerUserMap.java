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
name|jaxrs
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"IntegerUserMap"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"IntegerUserMap"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
specifier|public
class|class
name|IntegerUserMap
block|{
annotation|@
name|XmlElement
argument_list|(
name|nillable
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"entry"
argument_list|)
name|List
argument_list|<
name|IntegerUserEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|IntegerUserEntry
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|List
argument_list|<
name|IntegerUserEntry
argument_list|>
name|getEntries
parameter_list|()
block|{
return|return
name|entries
return|;
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"IdentifiedUser"
argument_list|)
specifier|static
class|class
name|IntegerUserEntry
block|{
comment|//Map keys cannot be null
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|nillable
operator|=
literal|false
argument_list|)
name|Integer
name|id
decl_stmt|;
name|User
name|user
decl_stmt|;
specifier|public
name|void
name|setId
parameter_list|(
name|Integer
name|k
parameter_list|)
block|{
name|id
operator|=
name|k
expr_stmt|;
block|}
specifier|public
name|Integer
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setUser
parameter_list|(
name|User
name|u
parameter_list|)
block|{
name|user
operator|=
name|u
expr_stmt|;
block|}
specifier|public
name|User
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
block|}
block|}
end_class

end_unit

