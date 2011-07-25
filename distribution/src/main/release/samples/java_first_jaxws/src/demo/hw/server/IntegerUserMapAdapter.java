begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|hw
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|adapters
operator|.
name|XmlAdapter
import|;
end_import

begin_class
specifier|public
class|class
name|IntegerUserMapAdapter
extends|extends
name|XmlAdapter
argument_list|<
name|IntegerUserMap
argument_list|,
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
argument_list|>
block|{
specifier|public
name|IntegerUserMap
name|marshal
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|v
parameter_list|)
throws|throws
name|Exception
block|{
name|IntegerUserMap
name|map
init|=
operator|new
name|IntegerUserMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|e
range|:
name|v
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|IntegerUserMap
operator|.
name|IntegerUserEntry
name|iue
init|=
operator|new
name|IntegerUserMap
operator|.
name|IntegerUserEntry
argument_list|()
decl_stmt|;
name|iue
operator|.
name|setUser
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|iue
operator|.
name|setId
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|getEntries
argument_list|()
operator|.
name|add
argument_list|(
name|iue
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
specifier|public
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|unmarshal
parameter_list|(
name|IntegerUserMap
name|v
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|IntegerUserMap
operator|.
name|IntegerUserEntry
name|e
range|:
name|v
operator|.
name|getEntries
argument_list|()
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|e
operator|.
name|getId
argument_list|()
argument_list|,
name|e
operator|.
name|getUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

