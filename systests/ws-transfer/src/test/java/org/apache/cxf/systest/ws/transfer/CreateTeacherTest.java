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
name|transfer
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
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
name|Document
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
name|staxutils
operator|.
name|StaxUtils
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
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
name|transfer
operator|.
name|Create
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
name|transfer
operator|.
name|CreateResponse
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
name|transfer
operator|.
name|Representation
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
name|transfer
operator|.
name|resourcefactory
operator|.
name|ResourceFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|CreateTeacherTest
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|CreateStudentTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT2
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|CreateStudentTest
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|RESOURCE_TEACHERS_URL
init|=
literal|"http://localhost:"
operator|+
name|PORT2
operator|+
literal|"/ResourceTeachers"
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
block|{
name|TestUtils
operator|.
name|createStudentsServers
argument_list|(
name|PORT
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
name|TestUtils
operator|.
name|createTeachersServers
argument_list|(
name|PORT2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|afterClass
parameter_list|()
block|{
name|TestUtils
operator|.
name|destroyStudentsServers
argument_list|()
expr_stmt|;
name|TestUtils
operator|.
name|destroyTeachersServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createTeacherTest
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|Document
name|createTeacherXML
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/xml/createTeacher.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Create
name|request
init|=
operator|new
name|Create
argument_list|()
decl_stmt|;
name|request
operator|.
name|setRepresentation
argument_list|(
operator|new
name|Representation
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|getRepresentation
argument_list|()
operator|.
name|setAny
argument_list|(
name|createTeacherXML
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|ResourceFactory
name|rf
init|=
name|TestUtils
operator|.
name|createResourceFactoryClient
argument_list|(
name|PORT
argument_list|)
decl_stmt|;
name|CreateResponse
name|response
init|=
name|rf
operator|.
name|create
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|RESOURCE_TEACHERS_URL
argument_list|,
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createTeacherPartialTest
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|Document
name|createTeacherPartialXML
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/xml/createTeacherPartial.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Create
name|request
init|=
operator|new
name|Create
argument_list|()
decl_stmt|;
name|request
operator|.
name|setRepresentation
argument_list|(
operator|new
name|Representation
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|getRepresentation
argument_list|()
operator|.
name|setAny
argument_list|(
name|createTeacherPartialXML
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|ResourceFactory
name|rf
init|=
name|TestUtils
operator|.
name|createResourceFactoryClient
argument_list|(
name|PORT
argument_list|)
decl_stmt|;
name|CreateResponse
name|response
init|=
name|rf
operator|.
name|create
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|RESOURCE_TEACHERS_URL
argument_list|,
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|SOAPFaultException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|createTeacherWrongTest
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|Document
name|createTeacherWrongXML
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/xml/createTeacherWrong.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Create
name|request
init|=
operator|new
name|Create
argument_list|()
decl_stmt|;
name|request
operator|.
name|setRepresentation
argument_list|(
operator|new
name|Representation
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|getRepresentation
argument_list|()
operator|.
name|setAny
argument_list|(
name|createTeacherWrongXML
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|ResourceFactory
name|rf
init|=
name|TestUtils
operator|.
name|createResourceFactoryClient
argument_list|(
name|PORT
argument_list|)
decl_stmt|;
name|rf
operator|.
name|create
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

