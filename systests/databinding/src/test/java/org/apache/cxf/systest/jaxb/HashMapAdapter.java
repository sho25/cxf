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
name|jaxb
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|XmlAttribute
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
name|XmlValue
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
name|adapters
operator|.
name|XmlAdapter
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|HashMapAdapter
extends|extends
name|XmlAdapter
argument_list|<
name|HashMapAdapter
operator|.
name|HashMapType
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
argument_list|>
block|{
annotation|@
name|XmlType
argument_list|()
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
specifier|public
specifier|static
class|class
name|HashMapEntryType
block|{
annotation|@
name|XmlAttribute
specifier|private
name|String
name|key
decl_stmt|;
annotation|@
name|XmlValue
specifier|private
name|byte
index|[]
name|value
decl_stmt|;
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|HashMapType
block|{
specifier|private
name|List
argument_list|<
name|HashMapEntryType
argument_list|>
name|entry
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|List
argument_list|<
name|HashMapEntryType
argument_list|>
name|getEntry
parameter_list|()
block|{
return|return
name|entry
return|;
block|}
specifier|public
name|void
name|setEntry
parameter_list|(
name|List
argument_list|<
name|HashMapEntryType
argument_list|>
name|entry
parameter_list|)
block|{
name|this
operator|.
name|entry
operator|=
name|entry
expr_stmt|;
block|}
block|}
specifier|public
name|HashMapType
name|marshal
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|arg0
parameter_list|)
throws|throws
name|Exception
block|{
name|HashMapType
name|myHashMapType
init|=
operator|new
name|HashMapType
argument_list|()
decl_stmt|;
if|if
condition|(
name|arg0
operator|!=
literal|null
operator|&&
operator|!
name|arg0
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|entry
range|:
name|arg0
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|HashMapEntryType
name|myHashEntryType
init|=
operator|new
name|HashMapEntryType
argument_list|()
decl_stmt|;
name|myHashEntryType
operator|.
name|key
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|myHashEntryType
operator|.
name|value
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|myHashMapType
operator|.
name|entry
operator|.
name|add
argument_list|(
name|myHashEntryType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|myHashMapType
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|unmarshal
parameter_list|(
name|HashMapType
name|arg0
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|hashMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|arg0
operator|!=
literal|null
operator|&&
name|arg0
operator|.
name|entry
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|HashMapEntryType
name|myHashEntryType
range|:
name|arg0
operator|.
name|entry
control|)
block|{
if|if
condition|(
name|myHashEntryType
operator|.
name|key
operator|!=
literal|null
condition|)
block|{
name|hashMap
operator|.
name|put
argument_list|(
name|myHashEntryType
operator|.
name|key
argument_list|,
name|myHashEntryType
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|hashMap
return|;
block|}
block|}
end_class

end_unit

