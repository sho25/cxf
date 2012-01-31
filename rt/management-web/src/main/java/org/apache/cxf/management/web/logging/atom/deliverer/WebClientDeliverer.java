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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|deliverer
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
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
name|abdera
operator|.
name|model
operator|.
name|Feed
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|Validate
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|jaxrs
operator|.
name|ext
operator|.
name|provider
operator|.
name|AtomEntryProvider
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
name|jaxrs
operator|.
name|ext
operator|.
name|provider
operator|.
name|AtomFeedProvider
import|;
end_import

begin_comment
comment|/**  * Marshaling and delivering based on JAXRS' WebClient.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|WebClientDeliverer
implements|implements
name|Deliverer
block|{
specifier|private
name|WebClient
name|wc
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|WebClientDeliverer
parameter_list|(
name|String
name|deliveryAddress
parameter_list|)
block|{
name|Validate
operator|.
name|notEmpty
argument_list|(
name|deliveryAddress
argument_list|,
literal|"deliveryAddress is empty or null"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|providers
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|AtomFeedProvider
argument_list|()
argument_list|,
operator|new
name|AtomEntryProvider
argument_list|()
argument_list|)
decl_stmt|;
name|wc
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|deliveryAddress
argument_list|,
name|providers
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WebClientDeliverer
parameter_list|(
name|WebClient
name|wc
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|wc
argument_list|,
literal|"wc is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|wc
operator|=
name|wc
expr_stmt|;
block|}
specifier|public
name|boolean
name|deliver
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|String
name|type
init|=
name|element
operator|instanceof
name|Feed
condition|?
literal|"application/atom+xml"
else|:
literal|"application/atom+xml;type=entry"
decl_stmt|;
name|wc
operator|.
name|type
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|Response
name|res
init|=
name|wc
operator|.
name|post
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|int
name|status
init|=
name|res
operator|.
name|getStatus
argument_list|()
decl_stmt|;
return|return
name|status
operator|>=
literal|200
operator|&&
name|status
operator|<=
literal|299
return|;
block|}
specifier|public
name|String
name|getEndpointAddress
parameter_list|()
block|{
return|return
name|wc
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

