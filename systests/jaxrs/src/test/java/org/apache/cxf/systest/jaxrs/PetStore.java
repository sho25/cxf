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
name|Collections
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
name|Consumes
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
name|POST
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
name|PathParam
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
name|core
operator|.
name|MediaType
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
name|MultivaluedMap
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
name|xml
operator|.
name|XMLName
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
class|class
name|PetStore
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CLOSED
init|=
literal|"The Pet Store is closed"
decl_stmt|;
specifier|public
name|PetStore
parameter_list|()
block|{     }
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|Response
name|getBaseStatus
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|CLOSED
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/petstore/pets/{petId}/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|getStatus
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"petId"
argument_list|)
name|String
name|petId
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|CLOSED
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/petstore/jaxb/status/"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|PetStoreStatus
name|getJaxbStatus
parameter_list|()
block|{
return|return
operator|new
name|PetStoreStatus
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/petstore/jaxb/status/elements"
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
literal|"text/xml"
block|,
literal|"application/json"
block|}
argument_list|)
annotation|@
name|XMLName
argument_list|(
literal|"{http://pets}statuses"
argument_list|)
specifier|public
name|List
argument_list|<
name|PetStoreStatusElement
argument_list|>
name|getJaxbStatusElements
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|PetStoreStatusElement
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/petstore/jaxb/status/element"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|PetStoreStatusElement
name|getJaxbStatusElement
parameter_list|()
block|{
return|return
operator|new
name|PetStoreStatusElement
argument_list|()
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/petstore/pets/"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Response
name|updateStatus
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
literal|"status"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"status"
argument_list|,
name|namespace
operator|=
literal|"http://pets"
argument_list|)
specifier|public
specifier|static
class|class
name|PetStoreStatus
block|{
specifier|private
name|String
name|status
init|=
name|PetStore
operator|.
name|CLOSED
decl_stmt|;
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
name|void
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
block|}
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"elstatus"
argument_list|,
name|namespace
operator|=
literal|"http://pets"
argument_list|)
specifier|public
specifier|static
class|class
name|PetStoreStatusElement
extends|extends
name|PetStoreStatus
block|{     }
block|}
end_class

end_unit

