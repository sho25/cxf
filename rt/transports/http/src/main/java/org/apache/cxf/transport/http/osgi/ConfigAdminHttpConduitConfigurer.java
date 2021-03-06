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
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
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
name|CopyOnWriteArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduitConfigurer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ManagedServiceFactory
import|;
end_import

begin_comment
comment|/**  * Collects configuration information using a ManagedServiceFactory.  *  * Registers a HTTPConduitConfigurer that can configure conduits based on the above  * configuration data.  *  * When used with felix config admin and fileinstall the configuration  * is expected in files named org.apache.cxf.http.conduits-XYZ.cfg  * that has a list of properties like:  *  * url: Regex url to match the configuration  * order: Integer order in which to apply the regex's when multiple regex's match.  * client.*  * tlsClientParameters.*  * proxyAuthorization.*  * authorization.*  *  * Where each of those is a prefix for the attributes that would be on the elements  * of the http:conduit configuration defined at:  *  * http://cxf.apache.org/schemas/configuration/http-conf.xsd  *  * For example:  * client.ReceiveTimeout: 1000  * authorization.Username: Foo  * tlsClientParameters.keyManagers.keyStore.file: mykeys.jks  * etc....  *  */
end_comment

begin_class
class|class
name|ConfigAdminHttpConduitConfigurer
implements|implements
name|ManagedServiceFactory
implements|,
name|HTTPConduitConfigurer
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FACTORY_PID
init|=
literal|"org.apache.cxf.http.conduits"
decl_stmt|;
comment|/**      * Stores the configuration data index by matcher and sorted by order      */
specifier|private
specifier|static
class|class
name|PidInfo
implements|implements
name|Comparable
argument_list|<
name|PidInfo
argument_list|>
block|{
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
decl_stmt|;
specifier|final
name|Matcher
name|matcher
decl_stmt|;
specifier|final
name|int
name|order
decl_stmt|;
name|PidInfo
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
parameter_list|,
name|Matcher
name|m
parameter_list|,
name|int
name|o
parameter_list|)
block|{
name|matcher
operator|=
name|m
expr_stmt|;
name|props
operator|=
name|p
expr_stmt|;
name|order
operator|=
name|o
expr_stmt|;
block|}
specifier|public
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProps
parameter_list|()
block|{
return|return
name|props
return|;
block|}
specifier|public
name|Matcher
name|getMatcher
parameter_list|()
block|{
return|return
name|matcher
return|;
block|}
specifier|public
name|int
name|compareTo
parameter_list|(
name|PidInfo
name|o
parameter_list|)
block|{
if|if
condition|(
name|order
operator|<
name|o
operator|.
name|order
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|order
operator|>
name|o
operator|.
name|order
condition|)
block|{
return|return
literal|1
return|;
block|}
comment|// priorities are equal
if|if
condition|(
name|matcher
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|o
operator|.
name|matcher
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|matcher
operator|.
name|pattern
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o
operator|.
name|matcher
operator|.
name|pattern
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|0
return|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|PidInfo
argument_list|>
name|props
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
literal|4
argument_list|,
literal|0.75f
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|CopyOnWriteArrayList
argument_list|<
name|PidInfo
argument_list|>
name|sorted
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|FACTORY_PID
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|updated
parameter_list|(
name|String
name|pid
parameter_list|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|Dictionary
name|properties
parameter_list|)
throws|throws
name|ConfigurationException
block|{
if|if
condition|(
name|pid
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|deleted
argument_list|(
name|pid
argument_list|)
expr_stmt|;
name|String
name|url
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
decl_stmt|;
name|String
name|name
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|url
operator|==
literal|null
condition|?
literal|null
else|:
name|Pattern
operator|.
name|compile
argument_list|(
name|url
argument_list|)
operator|.
name|matcher
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|String
name|p
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"order"
argument_list|)
decl_stmt|;
name|int
name|order
init|=
literal|50
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|order
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
name|PidInfo
name|info
init|=
operator|new
name|PidInfo
argument_list|(
name|properties
argument_list|,
name|matcher
argument_list|,
name|order
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|pid
argument_list|,
name|info
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
name|addToSortedInfos
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|addToSortedInfos
parameter_list|(
name|PidInfo
name|pi
parameter_list|)
block|{
name|int
name|size
init|=
name|sorted
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|size
condition|;
name|x
operator|++
control|)
block|{
name|PidInfo
name|p
init|=
name|sorted
operator|.
name|get
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
name|pi
operator|.
name|compareTo
argument_list|(
name|p
argument_list|)
operator|<
literal|0
condition|)
block|{
name|sorted
operator|.
name|add
argument_list|(
name|x
argument_list|,
name|pi
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|sorted
operator|.
name|add
argument_list|(
name|pi
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|removeFromSortedInfos
parameter_list|(
name|PidInfo
name|pi
parameter_list|)
block|{
name|sorted
operator|.
name|remove
argument_list|(
name|pi
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deleted
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
name|PidInfo
name|info
init|=
name|props
operator|.
name|remove
argument_list|(
name|pid
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|removeFromSortedInfos
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|d
init|=
name|info
operator|.
name|getProps
argument_list|()
decl_stmt|;
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
name|String
name|url
init|=
name|d
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|d
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|remove
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|configure
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|address
parameter_list|,
name|HTTPConduit
name|c
parameter_list|)
block|{
name|PidInfo
name|byName
init|=
literal|null
decl_stmt|;
name|PidInfo
name|byAddress
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|byName
operator|=
name|props
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|byAddress
operator|=
name|props
operator|.
name|get
argument_list|(
name|address
argument_list|)
expr_stmt|;
if|if
condition|(
name|byAddress
operator|==
name|byName
condition|)
block|{
name|byAddress
operator|=
literal|null
expr_stmt|;
block|}
block|}
name|HttpConduitConfigApplier
name|applier
init|=
operator|new
name|HttpConduitConfigApplier
argument_list|()
decl_stmt|;
for|for
control|(
name|PidInfo
name|info
range|:
name|sorted
control|)
block|{
if|if
condition|(
name|info
operator|.
name|getMatcher
argument_list|()
operator|!=
literal|null
operator|&&
name|info
operator|!=
name|byName
operator|&&
name|info
operator|!=
name|byAddress
condition|)
block|{
name|Matcher
name|m
init|=
name|info
operator|.
name|getMatcher
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|m
init|)
block|{
name|m
operator|.
name|reset
argument_list|(
name|address
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|applier
operator|.
name|apply
argument_list|(
name|info
operator|.
name|getProps
argument_list|()
argument_list|,
name|c
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|byAddress
operator|!=
literal|null
condition|)
block|{
name|applier
operator|.
name|apply
argument_list|(
name|byAddress
operator|.
name|getProps
argument_list|()
argument_list|,
name|c
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|byName
operator|!=
literal|null
condition|)
block|{
name|applier
operator|.
name|apply
argument_list|(
name|byName
operator|.
name|getProps
argument_list|()
argument_list|,
name|c
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

