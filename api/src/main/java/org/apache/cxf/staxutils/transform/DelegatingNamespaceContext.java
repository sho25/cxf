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
name|staxutils
operator|.
name|transform
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|namespace
operator|.
name|NamespaceContext
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
class|class
name|DelegatingNamespaceContext
implements|implements
name|NamespaceContext
block|{
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|prefixes
decl_stmt|;
specifier|private
name|NamespaceContext
name|nc
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
decl_stmt|;
specifier|public
name|DelegatingNamespaceContext
parameter_list|(
name|NamespaceContext
name|nc
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
parameter_list|)
block|{
name|this
operator|.
name|nc
operator|=
name|nc
expr_stmt|;
name|this
operator|.
name|nsMap
operator|=
name|nsMap
expr_stmt|;
name|this
operator|.
name|prefixes
operator|=
operator|new
name|LinkedList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|prefixes
operator|.
name|add
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|down
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pm
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefixes
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|pm
operator|.
name|putAll
argument_list|(
name|prefixes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|prefixes
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|pm
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|up
parameter_list|()
block|{
name|prefixes
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addPrefix
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|prefixes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|findUniquePrefix
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
if|if
condition|(
name|namespace
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|existingPrefix
init|=
name|prefixes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingPrefix
operator|!=
literal|null
condition|)
block|{
return|return
name|existingPrefix
return|;
block|}
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
operator|!
name|prefixes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|containsValue
argument_list|(
literal|"ps"
operator|+
operator|++
name|i
argument_list|)
condition|)
block|{
name|String
name|prefix
init|=
literal|"ps"
operator|+
name|i
decl_stmt|;
name|addPrefix
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
return|return
name|prefix
return|;
block|}
block|}
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|prefixes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
name|String
name|ns
init|=
name|nc
operator|.
name|getNamespaceURI
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns
operator|!=
literal|null
operator|&&
name|ns
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|addPrefix
argument_list|(
name|prefix
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
return|return
name|ns
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
if|if
condition|(
name|ns
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|value
init|=
name|nsMap
operator|.
name|get
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|actualNs
init|=
name|value
operator|==
literal|null
condition|?
name|ns
else|:
name|value
decl_stmt|;
if|if
condition|(
name|prefixes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|containsKey
argument_list|(
name|actualNs
argument_list|)
condition|)
block|{
return|return
name|prefixes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
name|actualNs
argument_list|)
return|;
block|}
name|String
name|prefix
init|=
name|nc
operator|.
name|getPrefix
argument_list|(
name|actualNs
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|addPrefix
argument_list|(
name|prefix
argument_list|,
name|actualNs
argument_list|)
expr_stmt|;
block|}
return|return
name|prefix
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|getPrefixes
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
name|nc
operator|.
name|getPrefixes
argument_list|(
name|ns
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

