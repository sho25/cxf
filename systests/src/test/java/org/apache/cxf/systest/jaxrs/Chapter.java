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
name|ws
operator|.
name|rs
operator|.
name|GET
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
name|Path
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
name|Produces
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
name|QueryParam
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
name|Context
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
name|UriInfo
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

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"Chapter"
argument_list|)
specifier|public
class|class
name|Chapter
block|{
specifier|private
name|String
name|title
decl_stmt|;
specifier|private
name|long
name|id
decl_stmt|;
specifier|public
name|Chapter
parameter_list|()
block|{     }
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|title
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|long
name|i
parameter_list|)
block|{
name|id
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/recurse"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|Chapter
name|getItself
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/xml;charset=ISO-8859-1"
argument_list|)
specifier|public
name|Chapter
name|get
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/matched-resources"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|getMatchedResources
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|ui
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|ui
operator|.
name|getMatchedResources
argument_list|()
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|obj
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/matched%21uris"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|getMatchedUris
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|ui
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"decode"
argument_list|)
name|String
name|decode
parameter_list|)
block|{
return|return
name|ui
operator|.
name|getMatchedURIs
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|decode
argument_list|)
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

