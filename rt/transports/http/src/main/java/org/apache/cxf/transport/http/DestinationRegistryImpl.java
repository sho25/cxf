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
name|transport
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_class
specifier|public
class|class
name|DestinationRegistryImpl
implements|implements
name|DestinationRegistry
block|{
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|AbstractHTTPDestination
argument_list|>
name|destinations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|AbstractHTTPDestination
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|AbstractHTTPDestination
argument_list|>
name|decodedDestinations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|AbstractHTTPDestination
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|DestinationRegistryImpl
parameter_list|()
block|{     }
specifier|public
name|void
name|addDestination
parameter_list|(
name|String
name|path
parameter_list|,
name|AbstractHTTPDestination
name|destination
parameter_list|)
block|{
name|String
name|p
init|=
name|getTrimmedPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|destinations
operator|.
name|putIfAbsent
argument_list|(
name|p
argument_list|,
name|destination
argument_list|)
expr_stmt|;
try|try
block|{
name|decodedDestinations
operator|.
name|put
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|p
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unsupported Encoding"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|removeDestination
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|destinations
operator|.
name|remove
argument_list|(
name|path
argument_list|)
expr_stmt|;
try|try
block|{
name|decodedDestinations
operator|.
name|remove
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|path
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unsupported Encoding"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|AbstractHTTPDestination
name|getDestinationForPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|getDestinationForPath
argument_list|(
name|path
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|AbstractHTTPDestination
name|getDestinationForPath
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|tryDecoding
parameter_list|)
block|{
comment|// to use the url context match
name|String
name|m
init|=
name|getTrimmedPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|AbstractHTTPDestination
name|s
init|=
name|destinations
operator|.
name|get
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|decodedDestinations
operator|.
name|get
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
specifier|public
name|AbstractHTTPDestination
name|checkRestfulRequest
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|int
name|len
init|=
operator|-
literal|1
decl_stmt|;
name|AbstractHTTPDestination
name|ret
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|path
range|:
name|getDestinationsPaths
argument_list|()
control|)
block|{
if|if
condition|(
name|address
operator|.
name|startsWith
argument_list|(
name|path
argument_list|)
operator|&&
name|path
operator|.
name|length
argument_list|()
operator|>
name|len
condition|)
block|{
name|ret
operator|=
name|getDestinationForPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|len
operator|=
name|path
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ret
operator|!=
literal|null
operator|&&
name|ret
operator|.
name|getMessageObserver
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|AbstractHTTPDestination
argument_list|>
name|getDestinations
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|destinations
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|AbstractHTTPDestination
argument_list|>
name|getSortedDestinations
parameter_list|()
block|{
name|List
argument_list|<
name|AbstractHTTPDestination
argument_list|>
name|dest2
init|=
operator|new
name|LinkedList
argument_list|<
name|AbstractHTTPDestination
argument_list|>
argument_list|(
name|getDestinations
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|dest2
argument_list|,
operator|new
name|Comparator
argument_list|<
name|AbstractHTTPDestination
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|AbstractHTTPDestination
name|o1
parameter_list|,
name|AbstractHTTPDestination
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|if
condition|(
name|o2
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
name|o1
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|dest2
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getDestinationsPaths
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|destinations
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Remove the transport protocol from the path and make       * it starts with /      * @param path       * @return trimmed path      */
specifier|public
name|String
name|getTrimmedPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|"/"
return|;
block|}
specifier|final
name|String
name|lh
init|=
literal|"http://localhost/"
decl_stmt|;
specifier|final
name|String
name|lhs
init|=
literal|"https://localhost/"
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|lh
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|lh
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|lhs
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|lhs
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|path
operator|.
name|contains
argument_list|(
literal|"://"
argument_list|)
operator|&&
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

